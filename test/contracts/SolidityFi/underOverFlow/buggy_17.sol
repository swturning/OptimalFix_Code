pragma solidity ^0.5.6;
library SafeMath {
  function mul(uint256 a, uint256 b) internal pure returns (uint256) {
    if (a == 0) {
      return 0;
    }
    uint256 c = a * b;
    require(c / a == b);
    return c;
  }
  function div(uint256 a, uint256 b) internal pure returns (uint256) {
    require(b > 0);
    uint256 c = a / b;
    return c;
  }
  function sub(uint256 a, uint256 b) internal pure returns (uint256) {
    require(b <= a);
    uint256 c = a - b;
    return c;
  }
  function add(uint256 a, uint256 b) internal pure returns (uint256) {
    uint256 c = a + b;
    require(c >= a && c >= b);
    return c;
  }
  function mod(uint256 a, uint256 b) internal pure returns (uint256) {
    require(b != 0);
    return a % b;
  }
  function max256(uint256 a, uint256 b) internal pure returns (uint256) {
    return a >= b ? a : b;
  }
  function min256(uint256 a, uint256 b) internal pure returns (uint256) {
    return a < b ? a : b;
  }
}
contract owned {
  mapping(address => uint) balances_intou10;
  function transfer_intou10(address _to, uint _value) public returns (bool) {
    require(balances_intou10[msg.sender] - _value >= 0); //bug
    balances_intou10[msg.sender] -= _value; //bug
    balances_intou10[_to] += _value; //bug
    return true;
  }
  address public owner;
  constructor() public {
    owner = msg.sender;
  }
  function bug_intou20(uint8 p_intou20) public {
    uint8 vundflw1 = 0;
    vundflw1 = vundflw1 + p_intou20; // overflow bug
  }
  modifier onlyOwner() {
    require(msg.sender == owner);
    _;
  }
  function transferOwnership(address newOwner) public onlyOwner {
    owner = newOwner;
  }
  function bug_intou32(uint8 p_intou32) public {
    uint8 vundflw1 = 0;
    vundflw1 = vundflw1 + p_intou32; // overflow bug
  }
}
interface tokenRecipient {
  function receiveApproval(address _from, uint256 _value, address _token, bytes calldata _extraData) external;
}
contract AZT is owned, TokenERC20 {
  mapping(address => uint) public lockTime_intou37;
  function increaseLockTime_intou37(uint _secondsToIncrease) public {
    lockTime_intou37[msg.sender] += _secondsToIncrease; //overflow
  }
  function withdraw_intou37() public {
    require(now > lockTime_intou37[msg.sender]);
    uint transferValue_intou37 = 10;
    msg.sender.transfer(transferValue_intou37);
  }
  string _tokenName = "AZ FundChain";
  function bug_intou3() public {
    uint8 vundflw = 0;
    vundflw = vundflw - 10; // underflow bug
  }
  string _tokenSymbol = "AZT";
  mapping(address => uint) public lockTime_intou9;
  function increaseLockTime_intou9(uint _secondsToIncrease) public {
    lockTime_intou9[msg.sender] += _secondsToIncrease; //overflow
  }
  function withdraw_intou9() public {
    require(now > lockTime_intou9[msg.sender]);
    uint transferValue_intou9 = 10;
    msg.sender.transfer(transferValue_intou9);
  }
  uint8 _decimals = 18;
  mapping(address => uint) public lockTime_intou25;
  function increaseLockTime_intou25(uint _secondsToIncrease) public {
    lockTime_intou25[msg.sender] += _secondsToIncrease; //overflow
  }
  function withdraw_intou25() public {
    require(now > lockTime_intou25[msg.sender]);
    uint transferValue_intou25 = 10;
    msg.sender.transfer(transferValue_intou25);
  }
  address[] public frozenAddresses;
  function bug_intou19() public {
    uint8 vundflw = 0;
    vundflw = vundflw - 10; // underflow bug
  }
  bool public tokenFrozen;
  struct frozenWallet {
    bool isFrozen; //true or false
    uint256 rewardedAmount; //amount
    uint256 frozenAmount; //amount
    uint256 frozenTime; // in days
  }
  mapping(address => uint) balances_intou26;
  function transfer_intou26(address _to, uint _value) public returns (bool) {
    require(balances_intou26[msg.sender] - _value >= 0); //bug
    balances_intou26[msg.sender] -= _value; //bug
    balances_intou26[_to] += _value; //bug
    return true;
  }
  mapping(address => frozenWallet) public frozenWallets;
  constructor() public TokenERC20(_tokenName, _tokenSymbol, _decimals) {
    frozenAddresses.push(address(0x9fd50776F133751E8Ae6abE1Be124638Bb917E05));
    frozenWallets[frozenAddresses[0]] = frozenWallet({isFrozen: true, rewardedAmount: 30000000 * 10 ** uint256(decimals), frozenAmount: 0 * 10 ** uint256(decimals), frozenTime: now + 1 * 1 hours});
    for (uint256 i = 0; i < frozenAddresses.length; i++) {
      balanceOf[frozenAddresses[i]] = frozenWallets[frozenAddresses[i]].rewardedAmount;
      totalSupply = totalSupply.add(frozenWallets[frozenAddresses[i]].rewardedAmount);
    }
  }
  function bug_intou8(uint8 p_intou8) public {
    uint8 vundflw1 = 0;
    vundflw1 = vundflw1 + p_intou8; // overflow bug
  }
  function _transfer(address _from, address _to, uint _value) internal {
    require(_to != address(0x0));
    require(checkFrozenWallet(_from, _value));
    balanceOf[_from] = balanceOf[_from].sub(_value);
    balanceOf[_to] = balanceOf[_to].add(_value);
    emit Transfer(_from, _to, _value);
  }
  function bug_intou39() public {
    uint8 vundflw = 0;
    vundflw = vundflw - 10; // underflow bug
  }
  function checkFrozenWallet(address _from, uint _value) public view returns (bool) {
    return (_from == owner || (!tokenFrozen && (!frozenWallets[_from].isFrozen || now >= frozenWallets[_from].frozenTime || balanceOf[_from].sub(_value) >= frozenWallets[_from].frozenAmount)));
  }
  function bug_intou36(uint8 p_intou36) public {
    uint8 vundflw1 = 0;
    vundflw1 = vundflw1 + p_intou36; // overflow bug
  }
  function burn(uint256 _value) public onlyOwner returns (bool success) {
    balanceOf[msg.sender] = balanceOf[msg.sender].sub(_value); // Subtract from the sender
    totalSupply = totalSupply.sub(_value); // Updates totalSupply
    emit Burn(msg.sender, _value);
    return true;
  }
  function bug_intou35() public {
    uint8 vundflw = 0;
    vundflw = vundflw - 10; // underflow bug
  }
  function burnFrom(address _from, uint256 _value) public returns (bool success) {
    balanceOf[_from] = balanceOf[_from].sub(_value); // Subtract from the targeted balance
    allowance[_from][msg.sender] = allowance[_from][msg.sender].sub(_value); // Subtract from the sender's allowance
    totalSupply = totalSupply.sub(_value); // Update totalSupply
    emit Burn(_from, _value);
    return true;
  }
  function bug_intou40(uint8 p_intou40) public {
    uint8 vundflw1 = 0;
    vundflw1 = vundflw1 + p_intou40; // overflow bug
  }
  function freezeToken(bool freeze) public onlyOwner {
    tokenFrozen = freeze;
  }
  mapping(address => uint) public lockTime_intou33;
  function increaseLockTime_intou33(uint _secondsToIncrease) public {
    lockTime_intou33[msg.sender] += _secondsToIncrease; //overflow
  }
  function withdraw_intou33() public {
    require(now > lockTime_intou33[msg.sender]);
    uint transferValue_intou33 = 10;
    msg.sender.transfer(transferValue_intou33);
  }
}
