pragma solidity ^0.5.12;

contract SimpleSuicide {
  address owner;

  constructor() {
    owner = msg.sender;
  }

  // <yes> <report> ACCESS_CONTROL
  //RPuc
  function sudicideAnyone() {
    selfdestruct(msg.sender);
  }
}
