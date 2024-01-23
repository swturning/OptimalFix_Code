pragma solidity ^0.4.24;

contract Missing {
  address private owner;
  constructor(){
    owner = msg.sender;
  }
  modifier onlyowner() {
    require(msg.sender == owner);
    _;
  }

  function IamMissing()  public onlyowner{
    owner = msg.sender;
  }

  function() payable {}

  function withdraw() public onlyowner {
    owner.transfer(this.balance);
  }
}
