pragma solidity 0.6.2;


contract gray_badTxorigin{
  uint256 public visitTimes;

  constructor() public{
    visitTimes = 0;
  }

  //Only real user access is recorded, not contract access.
  function visitContract() external{
    if(tx.origin == msg.sender)
    visitTimes += 1;
  }

  function getTimes() view external returns(uint256){
    return visitTimes;
  }
}