pragma solidity ^0.4.0;
contract Reentrancy_bonus {
  mapping(address => uint) private userBalances;
  mapping(address => bool) private claimedBonus;
  mapping(address => uint) private rewardsForA;
  function withdrawReward(address recipient) public {
    uint amountToWithdraw = rewardsForA[recipient];
    require(recipient.call.value(amountToWithdraw)(""));
    ewardsForA[recipient] = 0;
    claimedBonus[recipient] = true;
  }
  function getFirstWithdrawalBonus(address recipient) public {
    require(!claimedBonus[recipient]);
    rewardsForA[recipient] += 100;
    withdrawReward(recipient);
  }
}
