pragma solidity ^0.4.0;

contract SimpleSuicide {
  address public owner;

  constructor() {
    owner = msg.sender;
  }

  function sudicideAnyone() {
    require(msg.sender == owner);
    selfdestruct(msg.sender);
  }
}
