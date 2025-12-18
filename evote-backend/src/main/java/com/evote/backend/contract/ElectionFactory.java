package com.evote.backend.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
//import org.web3j.abi.datatypes.CustomError;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/LFDT-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.7.0.
 */
@SuppressWarnings("rawtypes")
public class ElectionFactory extends Contract {
    public static final String BINARY = "0x60803461010e5761223b906001600160401b0390601f38849003908101601f1916820190838211838310176100e3578083916040968794855283398101031261010e57610057602061005083610112565b9201610112565b6001600160a01b03918216919082156100f7575f80546001600160a01b031980821686178355949182918516907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09080a38360035416176003558451936020850190858210908211176100e3578552168092526004541617600455600180555161211490816101278239f35b634e487b7160e01b5f52604160045260245ffd5b8451631e4fbdf760e01b81525f6004820152602490fd5b5f80fd5b51906001600160a01b038216820361010e5756fe6080604052600436101562000012575f80fd5b5f3560e01c80632776af8b14620007fb57806336266ee814620007d1578063579a6988146200079b5780635e6fef011462000152578063715018a614620007405780637cdfbc2f14620003025780638da5cb5b14620002d9578063997d283014620002ba5780639d194cf01462000248578063a21d131c14620001d7578063a7c1abe014620001ad578063bd12dd641462000186578063dab50eb214620001525763f2fde38b14620000c2575f80fd5b346200014e5760203660031901126200014e57620000df6200088e565b620000e9620008c5565b6001600160a01b0390811690811562000136575f54826001600160601b0360a01b8216175f55167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e05f80a3005b604051631e4fbdf760e01b81525f6004820152602490fd5b5f80fd5b346200014e5760203660031901126200014e576004355f526002602052602060018060a01b0360405f205416604051908152f35b346200014e575f3660031901126200014e57602060ff60035460a01c166040519015158152f35b346200014e575f3660031901126200014e576004546040516001600160a01b039091168152602090f35b346200014e5760203660031901126200014e576004358015158091036200014e5760207ffce805cba192a0e94e99b9309e2e9e2d36cd03bdc97ff6fe47fa5c2ea374d0589162000226620008c5565b6003805460ff60a01b191660a083901b60ff60a01b16179055604051908152a1005b346200014e5760203660031901126200014e577f799652137a2d451f5b118f648da344343fe18f7c40bf1d528cc7defdf8e009516020620002886200088e565b62000292620008c5565b600480546001600160a01b0319166001600160a01b03929092169182179055604051908152a1005b346200014e575f3660031901126200014e576020600154604051908152f35b346200014e575f3660031901126200014e575f546040516001600160a01b039091168152602090f35b346200014e576101203660031901126200014e5760043567ffffffffffffffff81116200014e57366023820112156200014e5767ffffffffffffffff8160040135116200014e573660248260040135830101116200014e5760c4356001600160a01b0381168082036200014e5760e435916001600160a01b03831683036200014e57600354916001600160a01b0383163303620006ec5760ff8360a01c16620006a757846004013515620006625760a43560843510156200061d574260a4351115620005d857620005d157506004546001600160a01b0316915b604051601f1967ffffffffffffffff6004870135601f018216603f018216830190811190831117620005a9576004860135601f81018216603f0190911682016040528082526020820191906024870183375f60208760040135830101525190209160015493600185018511620005bd576001850160015560405192836117ed81011067ffffffffffffffff6117ed86011117620005a95783926117ed620008f285396117ed840187815261018060208201819052620004a5910160048a013560248b01620008a5565b6117ed8501604081018890526024356060820152604435608082015260643560a082015260843560c082015260a43560e08201526001600160a01b0394851661010082015291841661012083015291909216610140830152610104356101609092019190915203905ff09283156200059e57827f04d79b78a17f1d831bdecfa4ec28aee344c1b874ab5b3123259998a1a78a22166200058460209660018060a01b031693835f526002885260405f20856001600160601b0360a01b825416179055604051918291606083526060830190602481600401359101620008a5565b6084358983015260a43560408301520390a4604051908152f35b6040513d5f823e3d90fd5b634e487b7160e01b5f52604160045260245ffd5b634e487b7160e01b5f52601160045260245ffd5b91620003dc565b60405162461bcd60e51b815260206004820152601860248201527f466163746f72793a20656e6454696d6520696e207061737400000000000000006044820152606490fd5b60405162461bcd60e51b815260206004820152601c60248201527f466163746f72793a20696e76616c69642074696d652077696e646f77000000006044820152606490fd5b60405162461bcd60e51b815260206004820152601960248201527f466163746f72793a2069706673436964207265717569726564000000000000006044820152606490fd5b60405162461bcd60e51b815260206004820152601860248201527f466163746f72793a206372656174696f6e2070617573656400000000000000006044820152606490fd5b60405162461bcd60e51b815260206004820152602660248201527f466163746f72793a2063616c6c6572206e6f7420656c6563746f72616c417574604482015265686f7269747960d01b6064820152608490fd5b346200014e575f3660031901126200014e576200075c620008c5565b5f80546001600160a01b0319811682556001600160a01b03167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e08280a3005b346200014e5760203660031901126200014e576004355f526002602052602060018060a01b0360405f2054161515604051908152f35b346200014e575f3660031901126200014e576003546040516001600160a01b039091168152602090f35b346200014e5760203660031901126200014e57620008186200088e565b62000822620008c5565b6001600160a01b0316801562000849576001600160601b0360a01b60035416176003555f80f35b60405162461bcd60e51b815260206004820152601f60248201527f466163746f72793a20617574686f72697479207a65726f2061646472657373006044820152606490fd5b600435906001600160a01b03821682036200014e57565b908060209392818452848401375f828201840152601f01601f1916010190565b5f546001600160a01b03163303620008d957565b60405163118cdaa760e01b8152336004820152602490fdfe604060c08152346200041157620017ed803803806200001e8162000415565b92833981019061018081830312620004115780516020808301516001600160401b03939192908481116200041157820193601f93868587011215620004115785518281116200031057601f19966200007c8288018916840162000415565b98828a5283838301011162000411579082915f5b828110620003fc5750508801015f90528784015190606085015160808601519060a08701519260c08801519460e0890151966101008a01620000d2906200043b565b9b620000e26101208c016200043b565b9b620000f26101408d016200043b565b9b61016001519e600195867f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f0055600160a01b600190039e8f169c8d15620003b4578251156200036c578c8c10156200032457608052815194851162000310575f54938785811c9516801562000305575b82861014620002f157848487961162000298575b5081938511600114620002345750505f9262000228575b50505f19600383901b1c191690831b175f555b60a052556002556003556004556005558260018060a01b031994168460065416176006558360075416176007551690600854161760085560095561ffff19600c5416600c555161139c908162000451823960805181818161037a015281816103e0015281816105a301528181610bc801528181610e220152611161015260a0518161083e0152f35b015190505f806200018d565b879593929193165f8052835f20935f905b8282106200027e575050841162000265575b505050811b015f55620001a0565b01515f1960f88460031b161c191690555f808062000257565b848401518655899790950194938401939081019062000245565b9091929394505f8052825f208580880160051c820192858910620002e7575b9188978b9297969594930160051c01915b828110620002d857505062000176565b5f81558897508a9101620002c8565b92508192620002b7565b634e487b7160e01b5f52602260045260245ffd5b94607f169462000162565b634e487b7160e01b5f52604160045260245ffd5b509150508f60649250519062461bcd60e51b82526004820152601460248201527f456c656374696f6e3a206261642077696e646f770000000000000000000000006044820152fd5b509150508f60649250519062461bcd60e51b82526004820152601a60248201527f456c656374696f6e3a20697066734369642072657175697265640000000000006044820152fd5b509150508f60649250519062461bcd60e51b82526004820152601860248201527f456c656374696f6e3a20617574686f72697479207a65726f00000000000000006044820152fd5b8181018401518b820185015284930162000090565b5f80fd5b6040519190601f01601f191682016001600160401b038111838210176200031057604052565b51906001600160a01b0382168203620004115756fe6080806040526004361015610012575f80fd5b5f905f3560e01c908163051364d41461114c5750806311fbf2c11461112f57806322dc7b4c14611100578063258360b0146110e35780632eb4a7ab146110c65780633197cbb6146110a95780633a5f547a1461106c57806346401ed2146110475780634824b7e814610c52578063597e1fb514610c305780636c6c32d014610b62578063722f4a1f14610b4557806378e9792514610b285780637b5d253414610b005780637b91467414610a5e57806388c50c7614610998578063a2f52422146108a6578063aa7f08691461087e578063c521e02314610861578063c5a1d7f014610827578063d32642b9146102da578063d6717a43146101f9578063d729069d146101a3578063d7a6f6e814610185578063dc46d6ea1461015c5763fa6df55d1461013c575f80fd5b346101595780600319360112610159576020600254604051908152f35b80fd5b50346101595780600319360112610159576007546040516001600160a01b039091168152602090f35b50346101595780600319360112610159576020600354604051908152f35b5034610159576020366003190112610159576004356001600160a01b03818116918290036101f5576101da906007541633146112ad565b6bffffffffffffffffffffffff60a01b600654161760065580f35b8280fd5b50346101595780600319360112610159576040515f600e5461021a81611184565b808452906020906001908181169081156102b05750600114610257575b61025385610247818703826111d8565b604051918291826111fa565b0390f35b600e5f90815293507fbb7b4a454dc3493923482f07822329ed19e8244eff582cc204f8554c3620c3fd5b83851061029d5750505050810160200161024782610253610237565b8054868601840152938201938101610281565b8695506102539693506020925061024794915060ff191682840152151560051b8201019293610237565b503461066157610220366003190112610661576101a03660831901126106615760027f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f0054146108155760027f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f005560045442106107d857600554421161079e57600c5460ff81166107665760ff6103749160081c1615611241565b604080517f000000000000000000000000000000000000000000000000000000000000000060208201908152600435928201929092526024356060820152604435608082015260643560a08083019190915281526103d360c0826111d8565b51902060e43503610729577f000000000000000000000000000000000000000000000000000000000000000061010435036106ee5760015460a435036106a95760c4355f52600b60205260ff60405f2054166106655760018060a01b0360085416600954813b15610661575f916101c48392604051948593849263d0d898dd60e01b84526004840152608435602484015260a435604484015260c435606484015260e43560848401526101043560a484015261010061012460c48501375af1801561065657610627575b5060c4358152600b6020526040808220805460ff1916600117905551906104c3826111bc565b60043582526020820160243581526040830160443581526060840160643581526080850191428352600a54600160401b8110156106135780600161050a9201600a556112f9565b9490946105ff57600494959651865551600186015551600285015551600384015551910155600a545f1981019081116105eb57604051602081019060043582526024356040820152604435606082015260643560808201526080815261056f816111bc565b5190206040519081524260208201527f993fb86413e0a4b3c5a3480faa2b48cb49024d49ff3c627f7c36c16704ae6b7860407f000000000000000000000000000000000000000000000000000000000000000092a360017f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f005580f35b634e487b7160e01b82526011600452602482fd5b634e487b7160e01b86526004869052602486fd5b634e487b7160e01b86526041600452602486fd5b905067ffffffffffffffff8111610642576040525f8061049d565b634e487b7160e01b5f52604160045260245ffd5b6040513d5f823e3d90fd5b5f80fd5b606460405162461bcd60e51b815260206004820152602060248201527f456c656374696f6e3a206e756c6c696669657220616c726561647920757365646044820152fd5b60405162461bcd60e51b815260206004820152601d60248201527f456c656374696f6e3a206d65726b6c65526f6f74206d69736d617463680000006044820152606490fd5b60405162461bcd60e51b8152602060048201526013602482015272456c656374696f6e3a206261642073636f706560681b6044820152606490fd5b60405162461bcd60e51b8152602060048201526015602482015274456c656374696f6e3a20626164206d65737361676560581b6044820152606490fd5b60405162461bcd60e51b815260206004820152601060248201526f115b1958dd1a5bdb8e8818db1bdcd95960821b6044820152606490fd5b60405162461bcd60e51b8152602060048201526012602482015271115b1958dd1a5bdb8e88199a5b9a5cda195960721b6044820152606490fd5b60405162461bcd60e51b8152602060048201526015602482015274115b1958dd1a5bdb8e881b9bdd081cdd185c9d1959605a1b6044820152606490fd5b604051633ee5aeb560e01b8152600490fd5b34610661575f3660031901126106615760206040517f00000000000000000000000000000000000000000000000000000000000000008152f35b34610661575f366003190112610661576020600a54604051908152f35b34610661575f366003190112610661576006546040516001600160a01b039091168152602090f35b34610661575f3660031901126106615760ff600c5460081c16156109535760405180600d5480835260208093018091600d5f525f80516020611347833981519152905f5b8682821061093f578686610900828803836111d8565b60405192839281840190828552518091526040840192915f5b82811061092857505050500390f35b835185528695509381019392810192600101610919565b8354855290930192600192830192016108ea565b60405162461bcd60e51b815260206004820152601d60248201527f456c656374696f6e3a2074616c6c79206e6f74207075626c69736865640000006044820152606490fd5b3461066157602036600319011261066157600435600a54811015610a23576109bf906112f9565b50604051906109cd826111bc565b80548083526001820154602080850182905260028401546040808701829052600386015460608089018290526004909701546080988901819052825196875293860194909452840152928201529182015260a090f35b60405162461bcd60e51b815260206004820152601360248201527222b632b1ba34b7b71d1034b73232bc1027a7a160691b6044820152606490fd5b34610661575f366003190112610661576040515f8054610a7d81611184565b808452906020906001908181169081156102b05750600114610aa95761025385610247818703826111d8565b5f80805293507f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e5635b838510610aed5750505050810160200161024782610253610237565b8054868601840152938201938101610ad1565b34610661575f366003190112610661576008546040516001600160a01b039091168152602090f35b34610661575f366003190112610661576020600454604051908152f35b34610661575f366003190112610661576020600954604051908152f35b34610661575f36600319011261066157610b8760018060a01b036007541633146112ad565b600c5460ff8116610beb5760019060ff191617600c556040514281527fd3461d57d954b89a789e6fbd29ff23c87fef073eae45cc372d7e26cb1459435360207f000000000000000000000000000000000000000000000000000000000000000092a2005b60405162461bcd60e51b815260206004820152601860248201527f456c656374696f6e3a20616c726561647920636c6f73656400000000000000006044820152606490fd5b34610661575f36600319011261066157602060ff600c54166040519015158152f35b346106615760403660031901126106615760043567ffffffffffffffff808211610661573660238301121561066157816004013581811161066157602492838101928260051b9185833692010111610661578435948186116106615736602387011215610661578560040135918211610661578086019536828483010111610661576006546001600160a01b03163303610ff85760ff600c54610cfa828260081c1615611241565b168015610fed575b15610f9557600160401b8511610f8157600d5485600d55808610610f5e575b5090829186600d5f525f5b878110610f3a575050610d40600e54611184565b601f8111610ee1575b505f90601f8411600114610e56575f93610e49575b5050508160011b905f198360031b1c191617600e555b601f19601f820116604051610d8c60208301826111d8565b8281526020810190838883375f81850160200152519020600f55600c805461ff0019166101001790554260105560408051818152908101859052936001600160fb1b0310610661575f60808386948683977f15a054831771b8c6c68ce9eab7781b5645284a01ec828b20970d0c0f5aed5df59a60608901378601996060878c030160208801528160608c0152838b0137880101527f00000000000000000000000000000000000000000000000000000000000000009501030190a2005b0101359050868080610d5e565b91909250601f19841692600e5f52847fbb7b4a454dc3493923482f07822329ed19e8244eff582cc204f8554c3620c3fd945f5b818110610ec4575010610ea9575b50505050600181811b01600e55610d74565b5f1960f88660031b161c199201013516905585808080610e97565b858401850135875560019096019560209384019388935001610e89565b610f2a90600e5f527fbb7b4a454dc3493923482f07822329ed19e8244eff582cc204f8554c3620c3fd601f860160051c81019160208710610f30575b601f0160051c0190611297565b88610d49565b9091508190610f1d565b81355f80516020611347833981519152820155859450602090910190600101610d2c565b610f7b90865f805160206113478339815191529182019101611297565b87610d21565b50634e487b7160e01b5f9081526041600452fd5b60405162461bcd60e51b815260206004820152602b818401527f456c656374696f6e3a2063616e6e6f74207075626c6973682074616c6c79206260448201526a65666f726520636c6f736560a81b6064820152608490fd5b506005544211610d02565b60405162461bcd60e51b8152602060048201526022818401527f456c656374696f6e3a2063616c6c6572206e6f742074616c6c7956657269666960448201526132b960f11b6064820152608490fd5b34610661575f36600319011261066157602060ff600c5460081c166040519015158152f35b3461066157602036600319011261066157600435600d5481101561066157602090600d5f525f805160206113478339815191520154604051908152f35b34610661575f366003190112610661576020600554604051908152f35b34610661575f366003190112610661576020600154604051908152f35b34610661575f366003190112610661576020600f54604051908152f35b34610661576020366003190112610661576004355f52600b602052602060ff60405f2054166040519015158152f35b34610661575f366003190112610661576020601054604051908152f35b34610661575f366003190112610661576020907f00000000000000000000000000000000000000000000000000000000000000008152f35b90600182811c921680156111b2575b602083101461119e57565b634e487b7160e01b5f52602260045260245ffd5b91607f1691611193565b60a0810190811067ffffffffffffffff82111761064257604052565b90601f8019910116810190811067ffffffffffffffff82111761064257604052565b602080825282518183018190529093925f5b82811061122d57505060409293505f838284010152601f8019910116010190565b81810186015184820160400152850161120c565b1561124857565b60405162461bcd60e51b815260206004820152602160248201527f456c656374696f6e3a2074616c6c7920616c7265616479207075626c697368656044820152601960fa1b6064820152608490fd5b8181106112a2575050565b5f8155600101611297565b156112b457565b60405162461bcd60e51b815260206004820152601e60248201527f456c656374696f6e3a2063616c6c6572206e6f7420617574686f7269747900006044820152606490fd5b600a5481101561133257600590600a5f52027fc65a7bb8d6351c1cf70c95a316cc6a92839c986682d98bc35f958f4883f9d2a801905f90565b634e487b7160e01b5f52603260045260245ffdfed7b6990105719101dabeb77144f2a3385c8033acd3af97e9423a695e81ad1eb5a26469706673582212200870073b90f6f7e17fc36665da69bd5f70b8c15aed5e0da5a1941ce85f362be464736f6c63430008170033a2646970667358221220d604062106281c3111c1297dc4660bc94d2935adbd083c8f402892a6aa36e46d64736f6c63430008170033\n";

    private static String librariesLinkedBinary;

    public static final String FUNC_CREATEELECTION = "createElection";

    public static final String FUNC_CREATIONPAUSED = "creationPaused";

    public static final String FUNC_ELECTIONCOUNT = "electionCount";

    public static final String FUNC_ELECTIONS = "elections";

    public static final String FUNC_ELECTORALAUTHORITY = "electoralAuthority";

    public static final String FUNC_GETELECTIONADDRESS = "getElectionAddress";

    public static final String FUNC_GLOBALCONFIG = "globalConfig";

    public static final String FUNC_ISREGISTERED = "isRegistered";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SETCREATIONPAUSED = "setCreationPaused";

    public static final String FUNC_SETELECTORALAUTHORITY = "setElectoralAuthority";

    public static final String FUNC_SETGLOBALCONFIG = "setGlobalConfig";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event ELECTIONCREATED_EVENT = new Event("ElectionCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Bytes32>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event FACTORYPAUSED_EVENT = new Event("FactoryPaused", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
    ;

    public static final Event GLOBALCONFIGUPDATED_EVENT = new Event("GlobalConfigUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

//    public static final CustomError OWNABLEINVALIDOWNER_ERROR = new CustomError("OwnableInvalidOwner",
//            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
//    ;
//
//    public static final CustomError OWNABLEUNAUTHORIZEDACCOUNT_ERROR = new CustomError("OwnableUnauthorizedAccount",
//            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
//    ;

    @Deprecated
    protected ElectionFactory(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ElectionFactory(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ElectionFactory(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ElectionFactory(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> createElection(String ipfsCid, byte[] merkleRoot,
            BigInteger pkx, BigInteger pky, BigInteger startTime, BigInteger endTime,
            String tallyVerifier, String semaphore, BigInteger semaphoreGroupId) {
        final Function function = new Function(
                FUNC_CREATEELECTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ipfsCid), 
                new org.web3j.abi.datatypes.generated.Bytes32(merkleRoot), 
                new org.web3j.abi.datatypes.generated.Uint256(pkx), 
                new org.web3j.abi.datatypes.generated.Uint256(pky), 
                new org.web3j.abi.datatypes.generated.Uint256(startTime), 
                new org.web3j.abi.datatypes.generated.Uint256(endTime), 
                new org.web3j.abi.datatypes.Address(160, tallyVerifier), 
                new org.web3j.abi.datatypes.Address(160, semaphore), 
                new org.web3j.abi.datatypes.generated.Uint256(semaphoreGroupId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> creationPaused() {
        final Function function = new Function(FUNC_CREATIONPAUSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> electionCount() {
        final Function function = new Function(FUNC_ELECTIONCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> elections(BigInteger param0) {
        final Function function = new Function(FUNC_ELECTIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> electoralAuthority() {
        final Function function = new Function(FUNC_ELECTORALAUTHORITY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getElectionAddress(BigInteger electionId) {
        final Function function = new Function(FUNC_GETELECTIONADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(electionId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> globalConfig() {
        final Function function = new Function(FUNC_GLOBALCONFIG, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isRegistered(BigInteger electionId) {
        final Function function = new Function(FUNC_ISREGISTERED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(electionId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final Function function = new Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setCreationPaused(Boolean _paused) {
        final Function function = new Function(
                FUNC_SETCREATIONPAUSED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Bool(_paused)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setElectoralAuthority(String _authority) {
        final Function function = new Function(
                FUNC_SETELECTORALAUTHORITY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _authority)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setGlobalConfig(String _defaultTallyVerifier) {
        final Function function = new Function(
                FUNC_SETGLOBALCONFIG, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _defaultTallyVerifier)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static List<ElectionCreatedEventResponse> getElectionCreatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ELECTIONCREATED_EVENT, transactionReceipt);
        ArrayList<ElectionCreatedEventResponse> responses = new ArrayList<ElectionCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ElectionCreatedEventResponse typedResponse = new ElectionCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.electionId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.electionAddress = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.metadataHash = (byte[]) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.ipfsCid = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.startTime = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.endTime = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ElectionCreatedEventResponse getElectionCreatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ELECTIONCREATED_EVENT, log);
        ElectionCreatedEventResponse typedResponse = new ElectionCreatedEventResponse();
        typedResponse.log = log;
        typedResponse.electionId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.electionAddress = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.metadataHash = (byte[]) eventValues.getIndexedValues().get(2).getValue();
        typedResponse.ipfsCid = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.startTime = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.endTime = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<ElectionCreatedEventResponse> electionCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getElectionCreatedEventFromLog(log));
    }

    public Flowable<ElectionCreatedEventResponse> electionCreatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ELECTIONCREATED_EVENT));
        return electionCreatedEventFlowable(filter);
    }

    public static List<FactoryPausedEventResponse> getFactoryPausedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(FACTORYPAUSED_EVENT, transactionReceipt);
        ArrayList<FactoryPausedEventResponse> responses = new ArrayList<FactoryPausedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            FactoryPausedEventResponse typedResponse = new FactoryPausedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.paused = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static FactoryPausedEventResponse getFactoryPausedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(FACTORYPAUSED_EVENT, log);
        FactoryPausedEventResponse typedResponse = new FactoryPausedEventResponse();
        typedResponse.log = log;
        typedResponse.paused = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<FactoryPausedEventResponse> factoryPausedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getFactoryPausedEventFromLog(log));
    }

    public Flowable<FactoryPausedEventResponse> factoryPausedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(FACTORYPAUSED_EVENT));
        return factoryPausedEventFlowable(filter);
    }

    public static List<GlobalConfigUpdatedEventResponse> getGlobalConfigUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(GLOBALCONFIGUPDATED_EVENT, transactionReceipt);
        ArrayList<GlobalConfigUpdatedEventResponse> responses = new ArrayList<GlobalConfigUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            GlobalConfigUpdatedEventResponse typedResponse = new GlobalConfigUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.defaultTallyVerifier = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static GlobalConfigUpdatedEventResponse getGlobalConfigUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(GLOBALCONFIGUPDATED_EVENT, log);
        GlobalConfigUpdatedEventResponse typedResponse = new GlobalConfigUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.defaultTallyVerifier = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<GlobalConfigUpdatedEventResponse> globalConfigUpdatedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getGlobalConfigUpdatedEventFromLog(log));
    }

    public Flowable<GlobalConfigUpdatedEventResponse> globalConfigUpdatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GLOBALCONFIGUPDATED_EVENT));
        return globalConfigUpdatedEventFlowable(filter);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static OwnershipTransferredEventResponse getOwnershipTransferredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
        OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
        typedResponse.log = log;
        typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getOwnershipTransferredEventFromLog(log));
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    @Deprecated
    public static ElectionFactory load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new ElectionFactory(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ElectionFactory load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ElectionFactory(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ElectionFactory load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new ElectionFactory(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ElectionFactory load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ElectionFactory(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ElectionFactory> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider, String _electoralAuthority,
            String _defaultTallyVerifier) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _electoralAuthority), 
                new org.web3j.abi.datatypes.Address(160, _defaultTallyVerifier)));
        return deployRemoteCall(ElectionFactory.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<ElectionFactory> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider,
            String _electoralAuthority, String _defaultTallyVerifier) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _electoralAuthority), 
                new org.web3j.abi.datatypes.Address(160, _defaultTallyVerifier)));
        return deployRemoteCall(ElectionFactory.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<ElectionFactory> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit, String _electoralAuthority,
            String _defaultTallyVerifier) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _electoralAuthority), 
                new org.web3j.abi.datatypes.Address(160, _defaultTallyVerifier)));
        return deployRemoteCall(ElectionFactory.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<ElectionFactory> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,
            String _electoralAuthority, String _defaultTallyVerifier) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _electoralAuthority), 
                new org.web3j.abi.datatypes.Address(160, _defaultTallyVerifier)));
        return deployRemoteCall(ElectionFactory.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

//    public static void linkLibraries(List<Contract.LinkReference> references) {
//        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
//    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class ElectionCreatedEventResponse extends BaseEventResponse {
        public BigInteger electionId;

        public String electionAddress;

        public byte[] metadataHash;

        public String ipfsCid;

        public BigInteger startTime;

        public BigInteger endTime;
    }

    public static class FactoryPausedEventResponse extends BaseEventResponse {
        public Boolean paused;
    }

    public static class GlobalConfigUpdatedEventResponse extends BaseEventResponse {
        public String defaultTallyVerifier;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }
}
