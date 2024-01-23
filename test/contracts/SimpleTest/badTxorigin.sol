pragma solidity 0.6.2;

contract NobadTxorigin {
  address public owner;

  constructor() public payable {
    owner = msg.sender;
    require(msg.value > 0);
  }

  function withdraw() external {
    //patch pattern RPtx
    require(tx.origin == owner);
    msg.sender.transfer(address(this).balance);
  }
}
