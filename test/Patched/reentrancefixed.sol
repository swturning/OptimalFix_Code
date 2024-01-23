pragma solidity ^0.4.18;

contract Reentrance {
  mapping(address => uint) public balances;
  bool ReeTrAncy1 = false;

  function donate(address _to) public payable {
    balances[_to] += msg.value;
  }

  function balanceOf(address _who) public view returns (uint balance) {
    return balances[_who];
  }

  function withdraw(uint _amount) public {
    require(!ReeTrAncy1, "Reentrancy attempt detected");
    if (balances[msg.sender] >= _amount) {
      ReeTrAncy1 = true;
      if (msg.sender.call.value(_amount)()) {
        balances[msg.sender] -= _amount;
      }
      ReeTrAncy1 = false;
    }
  }

  function() public payable {}
}
