pragma solidity ^0.5.12;
contract SimpleSuicide {
  function sudicideAnyone() public {
    selfdestruct(msg.sender);
  }
}
