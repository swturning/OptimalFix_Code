pragma solidity 0.6.2;

contract gray_SuicideEasily2{
  address public owner;

  constructor() public payable{
    owner = msg.sender;
    require(msg.value > 0);
  }


  function withdraw() external {
    require(msg.sender = owner);
    msg.sender.transfer(address(this).balance);
    suicideSelf(msg.sender);
  }


}