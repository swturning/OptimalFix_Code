pragma solidity ^0.4.0;
contract Reentrancy_cross_function {
  bool reetrancy = false;
  mapping(address => uint) private userBalances;
  function transfer(address to, uint amount) {
    if (userBalances[msg.sender] >= amount) {
      userBalances[to] += amount;
      userBalances[msg.sender] -= amount;
    }
  }
  function withdrawBalance() public {
    uint amountToWithdraw = userBalances[msg.sender];
    require(!reetrancy);
    reetrancy = true;
    (bool success, ) = msg.sender.call.value(amountToWithdraw)(""); 
    // At this point, the caller's code is executed, and can call transfer()\
    reetrancy = false; 
    require(success);
    userBalances[msg.sender] = 0;
  }
}
