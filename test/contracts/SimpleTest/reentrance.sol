pragma solidity ^0.4.18;

contract Reentrance {
  mapping(address => uint) public balances;

  function donate(address _to) public payable {
    balances[_to] += msg.value;
  }

  function balanceOf(address _who) public view returns (uint balance) {
    return balances[_who];
  }

  function withdraw(uint _amount) public {
    if (balances[msg.sender] >= _amount) {
      //report reetrancy
      //patch pattern RPree-2
      if (msg.sender.call.value(_amount)()) {
        balances[msg.sender] -= _amount;
      }
    }
  }

  function() public payable {}
}