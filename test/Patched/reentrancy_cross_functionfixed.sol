pragma solidity ^0.4.0;

contract Reentrancy_cross_function {
  mapping(address => uint) private userBalances;

  function transfer(address to, uint amount) {
    if (userBalances[msg.sender] >= amount) {
      require(userBalances[to] + amount >= userBalances[to]);
      userBalances[to] += amount;
      userBalances[msg.sender] -= amount;
    }
  }

  function withdrawBalance() public {
    uint amountToWithdraw = userBalances[msg.sender];
    userBalances[msg.sender] = 0;
    bool success = msg.sender.call.value(amountToWithdraw)("");
    require(success);
  }
}
