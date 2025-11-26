require("@nomicfoundation/hardhat-toolbox");
require("dotenv").config();
/** @type import('hardhat/config').HardhatUserConfig */
module.exports = {
  solidity: "0.8.19",
  networks: {
    ganache: {
      // DEV : Ganache
      url: "http://127.0.0.1:7545",      // Ganache RPC Server
      accounts: [process.env.PRIVATE_KEY]   // my Ganache Private Key
    }
    // Prod
    //TODO: add the sepolia testnet network for production and change the netwrok flag when deploying
  }
};