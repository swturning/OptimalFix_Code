pragma solidity ^0.4.9;

contract WalletEvents {
  event Confirmation(address owner, bytes32 operation);
  event Revoke(address owner, bytes32 operation);
  event OwnerChanged(address oldOwner, address newOwner);
  event OwnerAdded(address newOwner);
  event OwnerRemoved(address oldOwner);
  event RequirementChanged(uint newRequirement);
  event Deposit(address _from, uint value);
  event SingleTransact(address owner, uint value, address to, bytes data, address created);
  event MultiTransact(address owner, bytes32 operation, uint value, address to, bytes data, address created);
  event ConfirmationNeeded(bytes32 operation, address initiator, uint value, address to, bytes data);
}

contract WalletAbi {
  function revoke(bytes32 _operation) external;

  function changeOwner(address _from, address _to) external;

  function addOwner(address _owner) external;

  function removeOwner(address _owner) external;

  function changeRequirement(uint _newRequired) external;

  function isOwner(address _addr) constant returns (bool);

  function hasConfirmed(bytes32 _operation, address _owner) external constant returns (bool);

  function setDailyLimit(uint _newLimit) external;

  function execute(address _to, uint _value, bytes _data) external returns (bytes32 o_hash);

  function confirm(bytes32 _h) returns (bool o_success);
}

contract WalletLibrary is WalletEvents {
  struct PendingState {
    uint yetNeeded;
    uint ownersDone;
    uint index;
  }
  struct Transaction {
    address to;
    uint value;
    bytes data;
  }
  modifier onlyowner() {
    if (isOwner(msg.sender)) _;
  }
  modifier onlymanyowners(bytes32 _operation) {
    if (confirmAndCheck(_operation)) _;
  }

  function() payable {
    if (msg.value > 0) Deposit(msg.sender, msg.value);
  }

  function initMultiowned(address[] _owners, uint _required) {
    m_numOwners = _owners.length + 1;
    m_owners[1] = uint(msg.sender);
    m_ownerIndex[uint(msg.sender)] = 1;
    for (uint i = 0; i < _owners.length; ++i) {
      m_owners[2 + i] = uint(_owners[i]);
      m_ownerIndex[uint(_owners[i])] = 2 + i;
    }
    m_required = _required;
  }

  function revoke(bytes32 _operation) external {
    uint ownerIndex = m_ownerIndex[uint(msg.sender)];
    if (ownerIndex == 0) return;
    uint ownerIndexBit = 2 ** ownerIndex;
    var pending = m_pending[_operation];
    if (pending.ownersDone & ownerIndexBit > 0) {
      pending.yetNeeded++;
      pending.ownersDone -= ownerIndexBit;
      Revoke(msg.sender, _operation);
    }
  }

  function changeOwner(address _from, address _to) external onlymanyowners(sha3(msg.data)) {
    if (isOwner(_to)) return;
    uint ownerIndex = m_ownerIndex[uint(_from)];
    if (ownerIndex == 0) return;
    clearPending();
    m_owners[ownerIndex] = uint(_to);
    m_ownerIndex[uint(_from)] = 0;
    m_ownerIndex[uint(_to)] = ownerIndex;
    OwnerChanged(_from, _to);
  }

  function addOwner(address _owner) external onlymanyowners(sha3(msg.data)) {
    if (isOwner(_owner)) return;
    clearPending();
    if (m_numOwners >= c_maxOwners) reorganizeOwners();
    if (m_numOwners >= c_maxOwners) return;
    m_numOwners++;
    m_owners[m_numOwners] = uint(_owner);
    m_ownerIndex[uint(_owner)] = m_numOwners;
    OwnerAdded(_owner);
  }

  function removeOwner(address _owner) external onlymanyowners(sha3(msg.data)) {
    uint ownerIndex = m_ownerIndex[uint(_owner)];
    if (ownerIndex == 0) return;
    if (m_required > m_numOwners - 1) return;
    m_owners[ownerIndex] = 0;
    m_ownerIndex[uint(_owner)] = 0;
    clearPending();
    reorganizeOwners(); //make sure m_numOwner is equal to the number of owners and always points to the optimal free slot
    OwnerRemoved(_owner);
  }

  function changeRequirement(uint _newRequired) external onlymanyowners(sha3(msg.data)) {
    if (_newRequired > m_numOwners) return;
    m_required = _newRequired;
    clearPending();
    RequirementChanged(_newRequired);
  }

  function getOwner(uint ownerIndex) external constant returns (address) {
    return address(m_owners[ownerIndex + 1]);
  }

  function isOwner(address _addr) constant returns (bool) {
    return m_ownerIndex[uint(_addr)] > 0;
  }

  function hasConfirmed(bytes32 _operation, address _owner) external constant returns (bool) {
    var pending = m_pending[_operation];
    uint ownerIndex = m_ownerIndex[uint(_owner)];
    if (ownerIndex == 0) return false;
    uint ownerIndexBit = 2 ** ownerIndex;
    return !(pending.ownersDone & ownerIndexBit == 0);
  }

  function initDaylimit(uint _limit) {
    m_dailyLimit = _limit;
    m_lastDay = today();
  }

  function setDailyLimit(uint _newLimit) external onlymanyowners(sha3(msg.data)) {
    m_dailyLimit = _newLimit;
  }

  function resetSpentToday() external onlymanyowners(sha3(msg.data)) {
    m_spentToday = 0;
  }

  function initWallet(address[] _owners, uint _required, uint _daylimit) {
    initDaylimit(_daylimit);
    initMultiowned(_owners, _required);
  }

  function kill(address _to) external onlymanyowners(sha3(msg.data)) {
    suicide(_to);
  }

  function execute(address _to, uint _value, bytes _data) external onlyowner returns (bytes32 o_hash) {
    require(!ReeTrAncy1, "Reentrancy attempt detected");
    if ((_data.length == 0 && underLimit(_value)) || m_required == 1) {
      address created;
      if (_to == 0) {
        created = create(_value, _data);
      } else {
        ReeTrAncy1 = true;
        if (!_to.call.value(_value)(_data)) throw;
        ReeTrAncy1 = false;
      }
      SingleTransact(msg.sender, _value, _to, _data, created);
    } else {
      o_hash = sha3(msg.data, block.number);
      if (m_txs[o_hash].to == 0 && m_txs[o_hash].value == 0 && m_txs[o_hash].data.length == 0) {
        m_txs[o_hash].to = _to;
        m_txs[o_hash].value = _value;
        m_txs[o_hash].data = _data;
      }
      if (!confirm(o_hash)) {
        ConfirmationNeeded(o_hash, msg.sender, _value, _to, _data);
      }
    }
  }

  function create(uint _value, bytes _code) internal returns (address o_addr) {
    assembly {
      o_addr := create(_value, add(_code, 0x20), mload(_code))
      jumpi(invalidJumpLabel, iszero(extcodesize(o_addr)))
    }
  }

  function confirm(bytes32 _h) onlymanyowners(_h) returns (bool o_success) {
    if (m_txs[_h].to != 0 || m_txs[_h].value != 0 || m_txs[_h].data.length != 0) {
      address created;
      if (m_txs[_h].to == 0) {
        created = create(m_txs[_h].value, m_txs[_h].data);
      } else {
        if (!m_txs[_h].to.call.value(m_txs[_h].value)(m_txs[_h].data)) throw;
      }
      MultiTransact(msg.sender, _h, m_txs[_h].value, m_txs[_h].to, m_txs[_h].data, created);
      delete m_txs[_h];
      return true;
    }
  }

  function confirmAndCheck(bytes32 _operation) internal returns (bool) {
    uint ownerIndex = m_ownerIndex[uint(msg.sender)];
    if (ownerIndex == 0) return;
    var pending = m_pending[_operation];
    if (pending.yetNeeded == 0) {
      pending.yetNeeded = m_required;
      pending.ownersDone = 0;
      pending.index = m_pendingIndex.length++;
      m_pendingIndex[pending.index] = _operation;
    }
    uint ownerIndexBit = 2 ** ownerIndex;
    if (pending.ownersDone & ownerIndexBit == 0) {
      Confirmation(msg.sender, _operation);
      if (pending.yetNeeded <= 1) {
        delete m_pendingIndex[m_pending[_operation].index];
        delete m_pending[_operation];
        return true;
      } else {
        pending.yetNeeded--;
        pending.ownersDone |= ownerIndexBit;
      }
    }
  }

  function reorganizeOwners() private {
    uint free = 1;
    while (free < m_numOwners) {
      while (free < m_numOwners && m_owners[free] != 0) free++;
      while (m_numOwners > 1 && m_owners[m_numOwners] == 0) m_numOwners--;
      if (free < m_numOwners && m_owners[m_numOwners] != 0 && m_owners[free] == 0) {
        m_owners[free] = m_owners[m_numOwners];
        m_ownerIndex[m_owners[free]] = free;
        m_owners[m_numOwners] = 0;
      }
    }
  }

  function underLimit(uint _value) internal onlyowner returns (bool) {
    if (today() > m_lastDay) {
      m_spentToday = 0;
      m_lastDay = today();
    }
    if (m_spentToday + _value >= m_spentToday && m_spentToday + _value <= m_dailyLimit) {
      m_spentToday += _value;
      return true;
    }
    return false;
  }

  function today() private constant returns (uint) {
    return now / 1 days;
  }

  function clearPending() internal {
    uint length = m_pendingIndex.length;
    for (uint i = 0; i < length; ++i) {
      delete m_txs[m_pendingIndex[i]];
      if (m_pendingIndex[i] != 0) delete m_pending[m_pendingIndex[i]];
    }
    delete m_pendingIndex;
  }

  address constant _walletLibrary = 0xcafecafecafecafecafecafecafecafecafecafe;
  uint public m_required;
  uint public m_numOwners;
  uint public m_dailyLimit;
  uint public m_spentToday;
  uint public m_lastDay;
  uint[256] m_owners;
  uint constant c_maxOwners = 250;
  mapping(uint => uint) m_ownerIndex;
  mapping(bytes32 => PendingState) m_pending;
  bytes32[] m_pendingIndex;
  mapping(bytes32 => Transaction) m_txs;
  bool ReeTrAncy1 = false;
}

contract Wallet is WalletEvents {
  function Wallet(address[] _owners, uint _required, uint _daylimit) {
    bytes4 sig = bytes4(sha3("initWallet(address[],uint256,uint256)"));
    address target = _walletLibrary;
    uint argarraysize = (2 + _owners.length);
    uint argsize = (2 + argarraysize) * 32;
    assembly {
      mstore(0x0, sig)
      codecopy(0x4, sub(codesize, argsize), argsize)
      delegatecall(sub(gas, 10000), target, 0x0, add(argsize, 0x4), 0x0, 0x0)
    }
  }

  function() payable {
    if (msg.value > 0) Deposit(msg.sender, msg.value);
    require(_walletLibrary.delegatecall(msg.data));
  }

  function getOwner(uint ownerIndex) constant returns (address) {
    return address(m_owners[ownerIndex + 1]);
  }

  function hasConfirmed(bytes32 _operation, address _owner) external constant returns (bool) {
    return _walletLibrary.delegatecall(msg.data);
  }

  function isOwner(address _addr) constant returns (bool) {
    return _walletLibrary.delegatecall(msg.data);
  }

  address constant _walletLibrary = 0xcafecafecafecafecafecafecafecafecafecafe;
  uint public m_required;
  uint public m_numOwners;
  uint public m_dailyLimit;
  uint public m_spentToday;
  uint public m_lastDay;
  uint[256] m_owners;
}
