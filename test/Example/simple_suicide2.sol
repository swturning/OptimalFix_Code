pragma solidity ^0.5.12;
contract SimpleSuicide {
  address contractOnwer;
  constructor() {
    contractOnwer = msg.sender;
  }
  function sudicideAnyone() public {
    selfdestruct(msg.sender);
  }
}
