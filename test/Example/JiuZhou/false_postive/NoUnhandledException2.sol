pragma solidity 0.6.2;
contract gray_xfBank {
  mapping(address => uint256) public ledger;
  address payable[] public user;
  address public bankOwner;
  address[] public Abnormal;
  constructor() public {
    bankOwner = msg.sender;
  }
  function deposit() external payable {
    require(msg.value > 0);
    ledger[msg.sender] += msg.value;
    user.push(msg.sender);
  }
  function withdraw(uint256 _money) external {
    require(ledger[msg.sender] >= _money);
    msg.sender.transfer(_money);
  }
  function refundAll() external {
    bool flag;
    require(msg.sender == bankOwner);
    uint256 _length = user.length;
    for (uint256 i = 0; i < _length; i++) {
      uint256 money = ledger[user[i]];
      ledger[user[i]] = 0;
      flag = user[i].send(money);
      if (flag == false) {
        Abnormal.push(user[i]);
        ledger[user[i]] = money;
      } else continue;
    }
  }
  function refundOthers() external {
    uint256 _length = Abnormal.length;
    for (uint256 i = 0; i < _length; i++) {
      if (Abnormal[i] == msg.sender) msg.sender.transfer(ledger[msg.sender]);
    }
  }
}
