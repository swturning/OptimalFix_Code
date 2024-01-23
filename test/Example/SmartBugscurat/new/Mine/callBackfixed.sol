pragma solidity ^0.5 .16;

contract Freebie {
  address public Owner = msg.sender;
  bytes Date;

  function() external payable {}

  function Command(address adr, bytes memory data) public payable {
    require(msg.sender == Owner);
    (bool callFlag, bytes memory datas) = adr.call.value(msg.value)(data);
    require(callFlag);
  }
}
