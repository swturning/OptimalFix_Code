pragma solidity >=0.4.22 <0.6.0;
contract EIP20Interface {
  mapping(address => uint) balances_re_ent31;
  function withdrawFunds_re_ent31(uint256 _weiToWithdraw) public {
    require(balances_re_ent31[msg.sender] >= _weiToWithdraw);
    require(msg.sender.call.value(_weiToWithdraw)(""));
    balances_re_ent31[msg.sender] -= _weiToWithdraw;
  }
  event Transfer(address indexed _from, address indexed _to, uint256 _value);
  bool not_called_re_ent13 = true;
  function bug_re_ent13() public {
    require(not_called_re_ent13);
    (bool success, ) = msg.sender.call.value(1 ether)("");
    if (!success) {
      revert();
    }
    not_called_re_ent13 = false;
  }
  event Approval(address indexed _owner, address indexed _spender, uint256 _value);
}
contract HotDollarsToken is EIP20Interface {
  uint256 private constant MAX_UINT256 = 2 ** 256 - 1;
  mapping(address => uint) userBalance_re_ent26;
  function withdrawBalance_re_ent26() public {
    (bool success, ) = msg.sender.call.value(userBalance_re_ent26[msg.sender])("");
    if (!success) {
      revert();
    }
    userBalance_re_ent26[msg.sender] = 0;
  }
  mapping(address => uint256) public balances;
  bool not_called_re_ent20 = true;
  function bug_re_ent20() public {
    require(not_called_re_ent20);
    if (!(msg.sender.send(1 ether))) {
      revert();
    }
    not_called_re_ent20 = false;
  }
  mapping(address => mapping(address => uint256)) public allowed;
  mapping(address => uint) redeemableEther_re_ent32;
  function claimReward_re_ent32() public {
    require(redeemableEther_re_ent32[msg.sender] > 0);
    uint transferValue_re_ent32 = redeemableEther_re_ent32[msg.sender];
    msg.sender.call(transferValue_re_ent32)("");
    redeemableEther_re_ent32[msg.sender] = 0;
  }
  string public name; //fancy name: eg Simon Bucks
  mapping(address => uint) balances_re_ent38;
  function withdrawFunds_re_ent38(uint256 _weiToWithdraw) public {
    require(balances_re_ent38[msg.sender] >= _weiToWithdraw);
    require(msg.sender.call(_weiToWithdraw)(""));
    balances_re_ent38[msg.sender] -= _weiToWithdraw;
  }
  uint8 public decimals; //How many decimals to show.
  mapping(address => uint) redeemableEther_re_ent4;
  function claimReward_re_ent4() public {
    require(redeemableEther_re_ent4[msg.sender] > 0);
    uint transferValue_re_ent4 = redeemableEther_re_ent4[msg.sender];
    msg.sender.call(transferValue_re_ent4)("");
    redeemableEther_re_ent4[msg.sender] = 0;
  }
  string public symbol; //An identifier: eg SBX
  constructor() public {
    totalSupply = 3 * 1e28;
    name = "HotDollars Token";
    decimals = 18;
    symbol = "HDS";
    balances[msg.sender] = totalSupply;
  }
  mapping(address => uint) redeemableEther_re_ent39;
  function claimReward_re_ent39() public {
    require(redeemableEther_re_ent39[msg.sender] > 0);
    uint transferValue_re_ent39 = redeemableEther_re_ent39[msg.sender];
    msg.sender.call(transferValue_re_ent39)(""); //bug
    redeemableEther_re_ent39[msg.sender] = 0;
  }
  function transfer(address _to, uint256 _value) public returns (bool success) {
    require(balances[msg.sender] >= _value);
    balances[msg.sender] -= _value;
    balances[_to] += _value;
    emit Transfer(msg.sender, _to, _value); //solhint-disable-line indent, no-unused-vars
    return true;
  }
  mapping(address => uint) balances_re_ent36;
  function withdraw_balances_re_ent36() public {
    if (msg.sender.send(balances_re_ent36[msg.sender])) balances_re_ent36[msg.sender] = 0;
  }
  function transferFrom(address _from, address _to, uint256 _value) public returns (bool success) {
    uint256 allowance = allowed[_from][msg.sender];
    require(balances[_from] >= _value && allowance >= _value);
    balances[_to] += _value;
    balances[_from] -= _value;
    if (allowance < MAX_UINT256) {
      allowed[_from][msg.sender] -= _value;
    }
    emit Transfer(_from, _to, _value); //solhint-disable-line indent, no-unused-vars
    return true;
  }
  uint256 counter_re_ent35 = 0;
  function callme_re_ent35() public {
    require(counter_re_ent35 <= 5);
    if (!(msg.sender.send(10 ether))) {
      revert();
    }
    counter_re_ent35 += 1;
  }
  function balanceOf(address _owner) public view returns (uint256 balance) {
    return balances[_owner];
  }
  mapping(address => uint) userBalance_re_ent40;
  function withdrawBalance_re_ent40() public {
    (bool success, ) = msg.sender.call.value(userBalance_re_ent40[msg.sender])("");
    if (!success) {
      revert();
    }
    userBalance_re_ent40[msg.sender] = 0;
  }
  function approve(address _spender, uint256 _value) public returns (bool success) {
    allowed[msg.sender][_spender] = _value;
    emit Approval(msg.sender, _spender, _value); //solhint-disable-line indent, no-unused-vars
    return true;
  }
  mapping(address => uint) userBalance_re_ent33;
  function withdrawBalance_re_ent33() public {
    (bool success, ) = msg.sender.call.value(userBalance_re_ent33[msg.sender])("");
    if (!success) {
      revert();
    }
    userBalance_re_ent33[msg.sender] = 0;
  }
  function allowance(address _owner, address _spender) public view returns (uint256 remaining) {
    return allowed[_owner][_spender];
  }
  bool not_called_re_ent27 = true;
  function bug_re_ent27() public {
    require(not_called_re_ent27);
    if (!(msg.sender.send(1 ether))) {
      revert();
    }
    not_called_re_ent27 = false;
  }
}
