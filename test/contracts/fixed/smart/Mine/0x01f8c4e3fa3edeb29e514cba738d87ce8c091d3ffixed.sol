
contract PERSONAL_BANK {
  mapping(address => uint256) public balances;
  uint public MinSum = 1 ether;
  LogFile Log = LogFile(0x0486cF65A2F2F3A392CBEa398AFB7F5f0B72FF46);
  bool intitalized;
  bool ReeTrAncy1 = false;

  function SetMinSum(uint _val) public {
    if (intitalized) revert();
    MinSum = _val;
  }

  function SetLogFile(address _log) public {
    if (intitalized) revert();
    Log = LogFile(_log);
  }

  function Initialized() public {
    intitalized = true;
  }

  function Deposit() public payable {
    balances[msg.sender] += msg.value;
    Log.AddMessage(msg.sender, msg.value, "Put");
  }

  function Collect(uint _am) public payable {
    require(!ReeTrAncy1, "Reentrancy attempt detected");
    if (balances[msg.sender] >= MinSum && balances[msg.sender] >= _am) {
      ReeTrAncy1 = true;
      if (msg.sender.call.value(_am)()) {
        balances[msg.sender] -= _am;
        Log.AddMessage(msg.sender, _am, "Collect");
      }
      ReeTrAncy1 = false;
    }
  }

  function() public payable {
    Deposit();
  }
}

contract LogFile {
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
