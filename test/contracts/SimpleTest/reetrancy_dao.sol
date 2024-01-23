/*
 * @source: https://consensys.github.io/smart-contract-best-practices/known_attacks/
 * @author: consensys
 * @vulnerable_at_lines: 24
 */

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
    //report reetrancy
    //patch pattern RPree-1
    require(msg.sender.call.value(amount)(""));
    transfer(amount);
  }

  function transfer(uint256 amount) public {
    userBalances[msg.sender] -= amount;
  }
}
