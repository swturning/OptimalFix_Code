pragma solidity ^0.4.0;
contract Reentrancy_cross_function {
  mapping(address => uint) private userBalances;
  function transfer(address to, uint amount) {
    if (userBalances[msg.sender] >= amount) {
      userBalances[to] += amount;
      userBalances[msg.sender] -= amount;
    }
  }
  function withdrawBalance(uint256 amount) public {
    require(msg.sender.call.value(amount)(""));
    transfer(amount);
  }
  function transfer(uint256 amount) public {
    userBalances[msg.sender] -= amount;
  }
}
