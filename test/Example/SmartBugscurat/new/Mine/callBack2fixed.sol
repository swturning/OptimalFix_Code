pragma solidity ^0.5 .16;

contract Greedy {
  address public Owner = msg.sender;
  bytes data;

  function() external payable {}

  function Command(address adr, bytes memory data) public payable {
    require(msg.sender == Owner);
    bool callFlag;
    (callFlag, data) = adr.call.value(msg.value)(data);
    require(callFlag);
  }
}
