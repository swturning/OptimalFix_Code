pragma solidity ^0.4.19;

contract ReentrancyDAO {
  mapping(address => uint) credit;
  uint balance;

  function withdrawAll() public {
    uint oCredit = credit[msg.sender];
    if (oCredit > 0) {
      require(balance >= oCredit);
      balance -= oCredit;
      credit[msg.sender] = 0;
      bool callResult = msg.sender.call.value(oCredit)();
      require(callResult);
    }
  }

  function deposit() public payable {
    credit[msg.sender] += msg.value;
    require(balance + msg.value >= balance);
    balance += msg.value;
  }
}
