pragma solidity ^0.5 .16;

contract Freebie {
  address public Owner = msg.sender;
  bytes Date;
  bool ReeTrAncy1 = false;

  function() external payable {}

  function Command(address adr, bytes memory data) public payable {
    require(!ReeTrAncy1, "Reentrancy attempt detected");
    require(msg.sender == Owner);
    ReeTrAncy1 = true;
    (bool callFlag, bytes memory datas) = adr.call.value(msg.value)(data);
    require(callFlag);
    ReeTrAncy1 = false;
    Date = datas;
  }
}
