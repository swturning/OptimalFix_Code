pragma solidity 0.6.2;
contract getWageNumber {

  uint256 public DailyWage;
  address public boss;
  constructor() public {
    DailyWage = 100;

    boss = msg.sender;
  }
  modifier onlyOwner() {
    require(msg.sender == boss);
    _;
  }
  function setDailyWage(uint256 _wage) external onlyOwner {
    DailyWage = _wage;
  }
  function calculateWage(uint256 dayNumber,uint256 coefficient) external view onlyOwner returns (uint256) {
    uint256 baseWage = DailyWage / coefficient;
    return baseWage * dayNumber;
  }
}
