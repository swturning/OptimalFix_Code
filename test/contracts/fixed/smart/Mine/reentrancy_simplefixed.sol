pragma solidity ^0.4.15;

contract Reentrance {
  mapping(address => uint) userBalance;
  bool ReeTrAncy1 = false;

  function getBalance(address u) constant returns (uint) {
    return userBalance[u];
  }

  function addToBalance() payable {
    userBalance[msg.sender] += msg.value;
  }

  function withdrawBalance() {
    require(!ReeTrAncy1, "Reentrancy attempt detected");
    ReeTrAncy1 = true;
    if (!(msg.sender.call.value(userBalance[msg.sender])())) {
      throw;
    }
    ReeTrAncy1 = false;
    userBalance[msg.sender] = 0;
  }
}
