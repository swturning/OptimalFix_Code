pragma solidity ^0.4.0;

contract SimpleSuicide {
  address public owner;

  constructor() {
    owner = msg.sender;
  }

  modifier isOwner() {
    require(owner == msg.sender, "address error");
    _;
  }

  function sudicideAnyone() public isOwner {
    selfdestruct(msg.sender);
  }
}
