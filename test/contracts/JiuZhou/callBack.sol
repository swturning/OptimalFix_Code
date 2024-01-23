/*
 * @source: etherscan.io 
 * @author: -
 * @vulnerable_at_lines: 39
 */

pragma solidity ^0.5.16;

contract Freebie
{
  address public Owner  = msg.sender;
  bytes Date;
  function() external payable{}

  function Command(address adr,bytes memory data)
  payable
  public
  {
    require(msg.sender == Owner);
    // <yes> <report> UNCHECKED_LL_CALLS
    (,bytes memory datas)= adr.call.value(msg.value)(data);
    Date = datas;
  }
}