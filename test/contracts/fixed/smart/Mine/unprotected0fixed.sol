pragma solidity ^0.4.15;

contract Unprotected {
  address private owner;
  modifier onlyowner() {
    require(msg.sender == owner);
    _;
  }

  constructor() {
    owner = msg.sender;
  }

  function changeOwner(address _newOwner) onlyowner public {
    owner = _newOwner;
  }

  function changeOwner_fixed(address _newOwner) public onlyowner {
    owner = _newOwner;
  }
}
