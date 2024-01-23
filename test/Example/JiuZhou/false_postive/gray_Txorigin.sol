pragma solidity 0.6.2;
contract gray_badTxorigin {
  uint256 public visitTimes;
  constructor() public {
    visitTimes = 0;
  }
  function visitContract() external {
    if (tx.origin == msg.sender) visitTimes += 1;
  }
  function getTimes() external view returns (uint256) {
    return visitTimes;
  }
}
