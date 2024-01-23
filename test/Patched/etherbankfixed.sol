pragma solidity ^0.4.0;

contract EtherBank {
  mapping(address => uint) userBalances;
  bool ReeTrAncy1 = false;

  function getBalance(address user) constant returns (uint) {
    return userBalances[user];
  }

  function addToBalance() {
    userBalances[msg.sender] += msg.value;
  }

  function withdrawBalance() {
    require(!ReeTrAncy1, "Reentrancy attempt detected");
    uint amountToWithdraw = userBalances[msg.sender];
    ReeTrAncy1 = true;
    if (!(msg.sender.call.value(amountToWithdraw)())) {
      throw;
    }
    ReeTrAncy1 = false;
    userBalances[msg.sender] = 0;
  }
}
