pragma solidity ^0.4.21;
import "./SafeMath.sol";

contract TokenSaleChallenge {
  using SafeMath for uint;
  mapping(address => uint256) public balanceOf;
  uint256 constant PRICE_PER_TOKEN = 1 ether;

  function TokenSaleChallenge(address _player) public payable {
    require(msg.value == 1 ether);
  }

  function isComplete() public view returns (bool) {
    return address(this).balance < 1 ether;
  }

  function buy(uint256 numTokens) public payable {
    require(msg.value >= numTokens.mul(PRICE_PER_TOKEN));
    balanceOf[msg.sender] = balanceOf[msg.sender].add(numTokens);
  }

  function sell(uint256 numTokens) public {
    require(balanceOf[msg.sender] >= numTokens);
    balanceOf[msg.sender] -= numTokens;
    msg.sender.transfer(numTokens * PRICE_PER_TOKEN);
  }
}
