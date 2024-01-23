/*
 * @source: etherscan.io 
 * @author: -
 * @vulnerable_at_lines: 39
 */

pragma solidity ^0.5.16;

contract Greedy
{
  address public Owner = msg.sender;
  bytes  data;
  function() external payable{}



  function Command(address adr,bytes memory data)
  payable
  public
  {
    require(msg.sender == Owner);

    (, data)= adr.call.value(msg.value)(data);
  }
}