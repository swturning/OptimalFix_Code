pragma solidity 0.6.2;

contract SuicideEasily {
  address public owner;

  constructor() public payable {
    owner = msg.sender;
    require(msg.value > 0);
  }

  modifier OnlyOwner() {
    require(msg.sender == owner);
    _;
  }

  function withdraw() external OnlyOwner {
    msg.sender.transfer(address(this).balance);
  }

  function suicideSelf(address payable _Beneficiary) external {
    require(msg.sender == owner);
    selfdestruct(_Beneficiary);
  }
}
