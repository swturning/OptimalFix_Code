pragma solidity ^0.4.19;

contract PrivateBank {
  mapping(address => uint) public balances;
  uint public MinDeposit = 1 ether;
  Log TransferLog;
  bool ReeTrAncy1 = false;

  function PrivateBank(address _lib) {
    TransferLog = Log(_lib);
  }

  function Deposit() public payable {
    if (msg.value >= MinDeposit) {
      balances[msg.sender] += msg.value;
      TransferLog.AddMessage(msg.sender, msg.value, "Deposit");
    }
  }

  function CashOut(uint _am) {
    require(!ReeTrAncy1, "Reentrancy attempt detected");
    if (_am <= balances[msg.sender]) {
      ReeTrAncy1 = true;
      if (msg.sender.call.value(_am)()) {
        balances[msg.sender] -= _am;
        TransferLog.AddMessage(msg.sender, _am, "CashOut");
      }
      ReeTrAncy1 = false;
    }
  }

  function() public payable {}
}

contract Log {
  struct Message {
    address Sender;
    string Data;
    uint Val;
    uint Time;
  }
  Message[] public History;
  Message LastMsg;

  function AddMessage(address _adr, uint _val, string _data) public {
    LastMsg.Sender = _adr;
    LastMsg.Time = now;
    LastMsg.Val = _val;
    LastMsg.Data = _data;
    History.push(LastMsg);
  }
}
