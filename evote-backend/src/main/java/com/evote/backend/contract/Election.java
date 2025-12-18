package com.evote.backend.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.Bool;
//import org.web3j.abi.datatypes.CustomError;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.StaticArray8;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.reflection.Parameterized;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple5;
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
public class Election extends Contract {
    public static final String BINARY = "0x604060c08152346200041157620017ed803803806200001e8162000415565b92833981019061018081830312620004115780516020808301516001600160401b03939192908481116200041157820193601f93868587011215620004115785518281116200031057601f19966200007c8288018916840162000415565b98828a5283838301011162000411579082915f5b828110620003fc5750508801015f90528784015190606085015160808601519060a08701519260c08801519460e0890151966101008a01620000d2906200043b565b9b620000e26101208c016200043b565b9b620000f26101408d016200043b565b9b61016001519e600195867f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f0055600160a01b600190039e8f169c8d15620003b4578251156200036c578c8c10156200032457608052815194851162000310575f54938785811c9516801562000305575b82861014620002f157848487961162000298575b5081938511600114620002345750505f9262000228575b50505f19600383901b1c191690831b175f555b60a052556002556003556004556005558260018060a01b031994168460065416176006558360075416176007551690600854161760085560095561ffff19600c5416600c555161139c908162000451823960805181818161037a015281816103e0015281816105a301528181610bc801528181610e220152611161015260a0518161083e0152f35b015190505f806200018d565b879593929193165f8052835f20935f905b8282106200027e575050841162000265575b505050811b015f55620001a0565b01515f1960f88460031b161c191690555f808062000257565b848401518655899790950194938401939081019062000245565b9091929394505f8052825f208580880160051c820192858910620002e7575b9188978b9297969594930160051c01915b828110620002d857505062000176565b5f81558897508a9101620002c8565b92508192620002b7565b634e487b7160e01b5f52602260045260245ffd5b94607f169462000162565b634e487b7160e01b5f52604160045260245ffd5b509150508f60649250519062461bcd60e51b82526004820152601460248201527f456c656374696f6e3a206261642077696e646f770000000000000000000000006044820152fd5b509150508f60649250519062461bcd60e51b82526004820152601a60248201527f456c656374696f6e3a20697066734369642072657175697265640000000000006044820152fd5b509150508f60649250519062461bcd60e51b82526004820152601860248201527f456c656374696f6e3a20617574686f72697479207a65726f00000000000000006044820152fd5b8181018401518b820185015284930162000090565b5f80fd5b6040519190601f01601f191682016001600160401b038111838210176200031057604052565b51906001600160a01b0382168203620004115756fe6080806040526004361015610012575f80fd5b5f905f3560e01c908163051364d41461114c5750806311fbf2c11461112f57806322dc7b4c14611100578063258360b0146110e35780632eb4a7ab146110c65780633197cbb6146110a95780633a5f547a1461106c57806346401ed2146110475780634824b7e814610c52578063597e1fb514610c305780636c6c32d014610b62578063722f4a1f14610b4557806378e9792514610b285780637b5d253414610b005780637b91467414610a5e57806388c50c7614610998578063a2f52422146108a6578063aa7f08691461087e578063c521e02314610861578063c5a1d7f014610827578063d32642b9146102da578063d6717a43146101f9578063d729069d146101a3578063d7a6f6e814610185578063dc46d6ea1461015c5763fa6df55d1461013c575f80fd5b346101595780600319360112610159576020600254604051908152f35b80fd5b50346101595780600319360112610159576007546040516001600160a01b039091168152602090f35b50346101595780600319360112610159576020600354604051908152f35b5034610159576020366003190112610159576004356001600160a01b03818116918290036101f5576101da906007541633146112ad565b6bffffffffffffffffffffffff60a01b600654161760065580f35b8280fd5b50346101595780600319360112610159576040515f600e5461021a81611184565b808452906020906001908181169081156102b05750600114610257575b61025385610247818703826111d8565b604051918291826111fa565b0390f35b600e5f90815293507fbb7b4a454dc3493923482f07822329ed19e8244eff582cc204f8554c3620c3fd5b83851061029d5750505050810160200161024782610253610237565b8054868601840152938201938101610281565b8695506102539693506020925061024794915060ff191682840152151560051b8201019293610237565b503461066157610220366003190112610661576101a03660831901126106615760027f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f0054146108155760027f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f005560045442106107d857600554421161079e57600c5460ff81166107665760ff6103749160081c1615611241565b604080517f000000000000000000000000000000000000000000000000000000000000000060208201908152600435928201929092526024356060820152604435608082015260643560a08083019190915281526103d360c0826111d8565b51902060e43503610729577f000000000000000000000000000000000000000000000000000000000000000061010435036106ee5760015460a435036106a95760c4355f52600b60205260ff60405f2054166106655760018060a01b0360085416600954813b15610661575f916101c48392604051948593849263d0d898dd60e01b84526004840152608435602484015260a435604484015260c435606484015260e43560848401526101043560a484015261010061012460c48501375af1801561065657610627575b5060c4358152600b6020526040808220805460ff1916600117905551906104c3826111bc565b60043582526020820160243581526040830160443581526060840160643581526080850191428352600a54600160401b8110156106135780600161050a9201600a556112f9565b9490946105ff57600494959651865551600186015551600285015551600384015551910155600a545f1981019081116105eb57604051602081019060043582526024356040820152604435606082015260643560808201526080815261056f816111bc565b5190206040519081524260208201527f993fb86413e0a4b3c5a3480faa2b48cb49024d49ff3c627f7c36c16704ae6b7860407f000000000000000000000000000000000000000000000000000000000000000092a360017f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f005580f35b634e487b7160e01b82526011600452602482fd5b634e487b7160e01b86526004869052602486fd5b634e487b7160e01b86526041600452602486fd5b905067ffffffffffffffff8111610642576040525f8061049d565b634e487b7160e01b5f52604160045260245ffd5b6040513d5f823e3d90fd5b5f80fd5b606460405162461bcd60e51b815260206004820152602060248201527f456c656374696f6e3a206e756c6c696669657220616c726561647920757365646044820152fd5b60405162461bcd60e51b815260206004820152601d60248201527f456c656374696f6e3a206d65726b6c65526f6f74206d69736d617463680000006044820152606490fd5b60405162461bcd60e51b8152602060048201526013602482015272456c656374696f6e3a206261642073636f706560681b6044820152606490fd5b60405162461bcd60e51b8152602060048201526015602482015274456c656374696f6e3a20626164206d65737361676560581b6044820152606490fd5b60405162461bcd60e51b815260206004820152601060248201526f115b1958dd1a5bdb8e8818db1bdcd95960821b6044820152606490fd5b60405162461bcd60e51b8152602060048201526012602482015271115b1958dd1a5bdb8e88199a5b9a5cda195960721b6044820152606490fd5b60405162461bcd60e51b8152602060048201526015602482015274115b1958dd1a5bdb8e881b9bdd081cdd185c9d1959605a1b6044820152606490fd5b604051633ee5aeb560e01b8152600490fd5b34610661575f3660031901126106615760206040517f00000000000000000000000000000000000000000000000000000000000000008152f35b34610661575f366003190112610661576020600a54604051908152f35b34610661575f366003190112610661576006546040516001600160a01b039091168152602090f35b34610661575f3660031901126106615760ff600c5460081c16156109535760405180600d5480835260208093018091600d5f525f80516020611347833981519152905f5b8682821061093f578686610900828803836111d8565b60405192839281840190828552518091526040840192915f5b82811061092857505050500390f35b835185528695509381019392810192600101610919565b8354855290930192600192830192016108ea565b60405162461bcd60e51b815260206004820152601d60248201527f456c656374696f6e3a2074616c6c79206e6f74207075626c69736865640000006044820152606490fd5b3461066157602036600319011261066157600435600a54811015610a23576109bf906112f9565b50604051906109cd826111bc565b80548083526001820154602080850182905260028401546040808701829052600386015460608089018290526004909701546080988901819052825196875293860194909452840152928201529182015260a090f35b60405162461bcd60e51b815260206004820152601360248201527222b632b1ba34b7b71d1034b73232bc1027a7a160691b6044820152606490fd5b34610661575f366003190112610661576040515f8054610a7d81611184565b808452906020906001908181169081156102b05750600114610aa95761025385610247818703826111d8565b5f80805293507f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e5635b838510610aed5750505050810160200161024782610253610237565b8054868601840152938201938101610ad1565b34610661575f366003190112610661576008546040516001600160a01b039091168152602090f35b34610661575f366003190112610661576020600454604051908152f35b34610661575f366003190112610661576020600954604051908152f35b34610661575f36600319011261066157610b8760018060a01b036007541633146112ad565b600c5460ff8116610beb5760019060ff191617600c556040514281527fd3461d57d954b89a789e6fbd29ff23c87fef073eae45cc372d7e26cb1459435360207f000000000000000000000000000000000000000000000000000000000000000092a2005b60405162461bcd60e51b815260206004820152601860248201527f456c656374696f6e3a20616c726561647920636c6f73656400000000000000006044820152606490fd5b34610661575f36600319011261066157602060ff600c54166040519015158152f35b346106615760403660031901126106615760043567ffffffffffffffff808211610661573660238301121561066157816004013581811161066157602492838101928260051b9185833692010111610661578435948186116106615736602387011215610661578560040135918211610661578086019536828483010111610661576006546001600160a01b03163303610ff85760ff600c54610cfa828260081c1615611241565b168015610fed575b15610f9557600160401b8511610f8157600d5485600d55808610610f5e575b5090829186600d5f525f5b878110610f3a575050610d40600e54611184565b601f8111610ee1575b505f90601f8411600114610e56575f93610e49575b5050508160011b905f198360031b1c191617600e555b601f19601f820116604051610d8c60208301826111d8565b8281526020810190838883375f81850160200152519020600f55600c805461ff0019166101001790554260105560408051818152908101859052936001600160fb1b0310610661575f60808386948683977f15a054831771b8c6c68ce9eab7781b5645284a01ec828b20970d0c0f5aed5df59a60608901378601996060878c030160208801528160608c0152838b0137880101527f00000000000000000000000000000000000000000000000000000000000000009501030190a2005b0101359050868080610d5e565b91909250601f19841692600e5f52847fbb7b4a454dc3493923482f07822329ed19e8244eff582cc204f8554c3620c3fd945f5b818110610ec4575010610ea9575b50505050600181811b01600e55610d74565b5f1960f88660031b161c199201013516905585808080610e97565b858401850135875560019096019560209384019388935001610e89565b610f2a90600e5f527fbb7b4a454dc3493923482f07822329ed19e8244eff582cc204f8554c3620c3fd601f860160051c81019160208710610f30575b601f0160051c0190611297565b88610d49565b9091508190610f1d565b81355f80516020611347833981519152820155859450602090910190600101610d2c565b610f7b90865f805160206113478339815191529182019101611297565b87610d21565b50634e487b7160e01b5f9081526041600452fd5b60405162461bcd60e51b815260206004820152602b818401527f456c656374696f6e3a2063616e6e6f74207075626c6973682074616c6c79206260448201526a65666f726520636c6f736560a81b6064820152608490fd5b506005544211610d02565b60405162461bcd60e51b8152602060048201526022818401527f456c656374696f6e3a2063616c6c6572206e6f742074616c6c7956657269666960448201526132b960f11b6064820152608490fd5b34610661575f36600319011261066157602060ff600c5460081c166040519015158152f35b3461066157602036600319011261066157600435600d5481101561066157602090600d5f525f805160206113478339815191520154604051908152f35b34610661575f366003190112610661576020600554604051908152f35b34610661575f366003190112610661576020600154604051908152f35b34610661575f366003190112610661576020600f54604051908152f35b34610661576020366003190112610661576004355f52600b602052602060ff60405f2054166040519015158152f35b34610661575f366003190112610661576020601054604051908152f35b34610661575f366003190112610661576020907f00000000000000000000000000000000000000000000000000000000000000008152f35b90600182811c921680156111b2575b602083101461119e57565b634e487b7160e01b5f52602260045260245ffd5b91607f1691611193565b60a0810190811067ffffffffffffffff82111761064257604052565b90601f8019910116810190811067ffffffffffffffff82111761064257604052565b602080825282518183018190529093925f5b82811061122d57505060409293505f838284010152601f8019910116010190565b81810186015184820160400152850161120c565b1561124857565b60405162461bcd60e51b815260206004820152602160248201527f456c656374696f6e3a2074616c6c7920616c7265616479207075626c697368656044820152601960fa1b6064820152608490fd5b8181106112a2575050565b5f8155600101611297565b156112b457565b60405162461bcd60e51b815260206004820152601e60248201527f456c656374696f6e3a2063616c6c6572206e6f7420617574686f7269747900006044820152606490fd5b600a5481101561133257600590600a5f52027fc65a7bb8d6351c1cf70c95a316cc6a92839c986682d98bc35f958f4883f9d2a801905f90565b634e487b7160e01b5f52603260045260245ffdfed7b6990105719101dabeb77144f2a3385c8033acd3af97e9423a695e81ad1eb5a26469706673582212200870073b90f6f7e17fc36665da69bd5f70b8c15aed5e0da5a1941ce85f362be464736f6c63430008170033\n";

    private static String librariesLinkedBinary;

    public static final String FUNC_CLOSEELECTION = "closeElection";

    public static final String FUNC_CLOSED = "closed";

    public static final String FUNC_ELECTIONAUTHORITY = "electionAuthority";

    public static final String FUNC_ELECTIONID = "electionId";

    public static final String FUNC_ENDTIME = "endTime";

    public static final String FUNC_FINALTALLY = "finalTally";

    public static final String FUNC_FINALTALLYPUBLISHED = "finalTallyPublished";

    public static final String FUNC_GETENCRYPTEDVOTE = "getEncryptedVote";

    public static final String FUNC_GETENCRYPTEDVOTESCOUNT = "getEncryptedVotesCount";

    public static final String FUNC_GETFINALTALLY = "getFinalTally";

    public static final String FUNC_IPFSCID = "ipfsCid";

    public static final String FUNC_ISNULLIFIERUSED = "isNullifierUsed";

    public static final String FUNC_MERKLEROOT = "merkleRoot";

    public static final String FUNC_METADATAHASH = "metadataHash";

    public static final String FUNC_ONTALLYVERIFIED = "onTallyVerified";

    public static final String FUNC_PUBLICKEYX = "publicKeyX";

    public static final String FUNC_PUBLICKEYY = "publicKeyY";

    public static final String FUNC_SEMAPHORE = "semaphore";

    public static final String FUNC_SEMAPHOREGROUPID = "semaphoreGroupId";

    public static final String FUNC_SETTALLYVERIFIER = "setTallyVerifier";

    public static final String FUNC_STARTTIME = "startTime";

    public static final String FUNC_SUBMITVOTE = "submitVote";

    public static final String FUNC_TALLYPROOFCID = "tallyProofCid";

    public static final String FUNC_TALLYPROOFCIDHASH = "tallyProofCidHash";

    public static final String FUNC_TALLYPUBLISHED = "tallyPublished";

    public static final String FUNC_TALLYVERIFIER = "tallyVerifier";

    public static final Event ELECTIONCLOSED_EVENT = new Event("ElectionClosed", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TALLYPUBLISHED_EVENT = new Event("TallyPublished", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event VOTESUBMITTED_EVENT = new Event("VoteSubmitted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}));
    ;

//    public static final CustomError REENTRANCYGUARDREENTRANTCALL_ERROR = new CustomError("ReentrancyGuardReentrantCall",
//            Arrays.<TypeReference<?>>asList());
//    ;

    @Deprecated
    protected Election(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Election(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Election(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Election(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> closeElection() {
        final Function function = new Function(
                FUNC_CLOSEELECTION, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> closed() {
        final Function function = new Function(FUNC_CLOSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> electionAuthority() {
        final Function function = new Function(FUNC_ELECTIONAUTHORITY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> electionId() {
        final Function function = new Function(FUNC_ELECTIONID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> endTime() {
        final Function function = new Function(FUNC_ENDTIME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> finalTally(BigInteger param0) {
        final Function function = new Function(FUNC_FINALTALLY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> finalTallyPublished() {
        final Function function = new Function(FUNC_FINALTALLYPUBLISHED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>> getEncryptedVote(
            BigInteger index) {
        final Function function = new Function(FUNC_GETENCRYPTEDVOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger> call()
                            throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getEncryptedVotesCount() {
        final Function function = new Function(FUNC_GETENCRYPTEDVOTESCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<List> getFinalTally() {
        final Function function = new Function(FUNC_GETFINALTALLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<String> ipfsCid() {
        final Function function = new Function(FUNC_IPFSCID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isNullifierUsed(byte[] nullifier) {
        final Function function = new Function(FUNC_ISNULLIFIERUSED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(nullifier)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<byte[]> merkleRoot() {
        final Function function = new Function(FUNC_MERKLEROOT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<byte[]> metadataHash() {
        final Function function = new Function(FUNC_METADATAHASH, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<TransactionReceipt> onTallyVerified(List<BigInteger> tallies,
            String proofCid) {
        final Function function = new Function(
                FUNC_ONTALLYVERIFIED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(tallies, org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.Utf8String(proofCid)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> publicKeyX() {
        final Function function = new Function(FUNC_PUBLICKEYX, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> publicKeyY() {
        final Function function = new Function(FUNC_PUBLICKEYY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> semaphore() {
        final Function function = new Function(FUNC_SEMAPHORE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> semaphoreGroupId() {
        final Function function = new Function(FUNC_SEMAPHOREGROUPID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setTallyVerifier(String _tallyVerifier) {
        final Function function = new Function(
                FUNC_SETTALLYVERIFIER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tallyVerifier)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> startTime() {
        final Function function = new Function(FUNC_STARTTIME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> submitVote(BigInteger c1x, BigInteger c1y,
            BigInteger c2x, BigInteger c2y, SemaphoreProof semaProof) {
        final Function function = new Function(
                FUNC_SUBMITVOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(c1x), 
                new org.web3j.abi.datatypes.generated.Uint256(c1y), 
                new org.web3j.abi.datatypes.generated.Uint256(c2x), 
                new org.web3j.abi.datatypes.generated.Uint256(c2y), 
                semaProof), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> tallyProofCid() {
        final Function function = new Function(FUNC_TALLYPROOFCID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<byte[]> tallyProofCidHash() {
        final Function function = new Function(FUNC_TALLYPROOFCIDHASH, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<Boolean> tallyPublished() {
        final Function function = new Function(FUNC_TALLYPUBLISHED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> tallyVerifier() {
        final Function function = new Function(FUNC_TALLYVERIFIER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static List<ElectionClosedEventResponse> getElectionClosedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ELECTIONCLOSED_EVENT, transactionReceipt);
        ArrayList<ElectionClosedEventResponse> responses = new ArrayList<ElectionClosedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ElectionClosedEventResponse typedResponse = new ElectionClosedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.electionId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ElectionClosedEventResponse getElectionClosedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ELECTIONCLOSED_EVENT, log);
        ElectionClosedEventResponse typedResponse = new ElectionClosedEventResponse();
        typedResponse.log = log;
        typedResponse.electionId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ElectionClosedEventResponse> electionClosedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getElectionClosedEventFromLog(log));
    }

    public Flowable<ElectionClosedEventResponse> electionClosedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ELECTIONCLOSED_EVENT));
        return electionClosedEventFlowable(filter);
    }

    public static List<TallyPublishedEventResponse> getTallyPublishedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TALLYPUBLISHED_EVENT, transactionReceipt);
        ArrayList<TallyPublishedEventResponse> responses = new ArrayList<TallyPublishedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TallyPublishedEventResponse typedResponse = new TallyPublishedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.electionId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.tallies = (List<BigInteger>) ((Array) eventValues.getNonIndexedValues().get(0)).getNativeValueCopy();
            typedResponse.proofCid = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TallyPublishedEventResponse getTallyPublishedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TALLYPUBLISHED_EVENT, log);
        TallyPublishedEventResponse typedResponse = new TallyPublishedEventResponse();
        typedResponse.log = log;
        typedResponse.electionId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.tallies = (List<BigInteger>) ((Array) eventValues.getNonIndexedValues().get(0)).getNativeValueCopy();
        typedResponse.proofCid = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<TallyPublishedEventResponse> tallyPublishedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTallyPublishedEventFromLog(log));
    }

    public Flowable<TallyPublishedEventResponse> tallyPublishedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TALLYPUBLISHED_EVENT));
        return tallyPublishedEventFlowable(filter);
    }

    public static List<VoteSubmittedEventResponse> getVoteSubmittedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTESUBMITTED_EVENT, transactionReceipt);
        ArrayList<VoteSubmittedEventResponse> responses = new ArrayList<VoteSubmittedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VoteSubmittedEventResponse typedResponse = new VoteSubmittedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.electionId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.voteIndex = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.cipherHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static VoteSubmittedEventResponse getVoteSubmittedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(VOTESUBMITTED_EVENT, log);
        VoteSubmittedEventResponse typedResponse = new VoteSubmittedEventResponse();
        typedResponse.log = log;
        typedResponse.electionId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.voteIndex = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.cipherHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<VoteSubmittedEventResponse> voteSubmittedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getVoteSubmittedEventFromLog(log));
    }

    public Flowable<VoteSubmittedEventResponse> voteSubmittedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTESUBMITTED_EVENT));
        return voteSubmittedEventFlowable(filter);
    }

    @Deprecated
    public static Election load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new Election(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Election load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Election(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Election load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new Election(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Election load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Election(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Election> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider, BigInteger _electionId, String _ipfsCid,
            byte[] _metadataHash, byte[] _merkleRoot, BigInteger _pkx, BigInteger _pky,
            BigInteger _startTime, BigInteger _endTime, String _tallyVerifier,
            String _electionAuthority, String _semaphore, BigInteger _semaphoreGroupId) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId), 
                new org.web3j.abi.datatypes.Utf8String(_ipfsCid), 
                new org.web3j.abi.datatypes.generated.Bytes32(_metadataHash), 
                new org.web3j.abi.datatypes.generated.Bytes32(_merkleRoot), 
                new org.web3j.abi.datatypes.generated.Uint256(_pkx), 
                new org.web3j.abi.datatypes.generated.Uint256(_pky), 
                new org.web3j.abi.datatypes.generated.Uint256(_startTime), 
                new org.web3j.abi.datatypes.generated.Uint256(_endTime), 
                new org.web3j.abi.datatypes.Address(160, _tallyVerifier), 
                new org.web3j.abi.datatypes.Address(160, _electionAuthority), 
                new org.web3j.abi.datatypes.Address(160, _semaphore), 
                new org.web3j.abi.datatypes.generated.Uint256(_semaphoreGroupId)));
        return deployRemoteCall(Election.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<Election> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider, BigInteger _electionId, String _ipfsCid,
            byte[] _metadataHash, byte[] _merkleRoot, BigInteger _pkx, BigInteger _pky,
            BigInteger _startTime, BigInteger _endTime, String _tallyVerifier,
            String _electionAuthority, String _semaphore, BigInteger _semaphoreGroupId) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId), 
                new org.web3j.abi.datatypes.Utf8String(_ipfsCid), 
                new org.web3j.abi.datatypes.generated.Bytes32(_metadataHash), 
                new org.web3j.abi.datatypes.generated.Bytes32(_merkleRoot), 
                new org.web3j.abi.datatypes.generated.Uint256(_pkx), 
                new org.web3j.abi.datatypes.generated.Uint256(_pky), 
                new org.web3j.abi.datatypes.generated.Uint256(_startTime), 
                new org.web3j.abi.datatypes.generated.Uint256(_endTime), 
                new org.web3j.abi.datatypes.Address(160, _tallyVerifier), 
                new org.web3j.abi.datatypes.Address(160, _electionAuthority), 
                new org.web3j.abi.datatypes.Address(160, _semaphore), 
                new org.web3j.abi.datatypes.generated.Uint256(_semaphoreGroupId)));
        return deployRemoteCall(Election.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Election> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit, BigInteger _electionId, String _ipfsCid,
            byte[] _metadataHash, byte[] _merkleRoot, BigInteger _pkx, BigInteger _pky,
            BigInteger _startTime, BigInteger _endTime, String _tallyVerifier,
            String _electionAuthority, String _semaphore, BigInteger _semaphoreGroupId) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId), 
                new org.web3j.abi.datatypes.Utf8String(_ipfsCid), 
                new org.web3j.abi.datatypes.generated.Bytes32(_metadataHash), 
                new org.web3j.abi.datatypes.generated.Bytes32(_merkleRoot), 
                new org.web3j.abi.datatypes.generated.Uint256(_pkx), 
                new org.web3j.abi.datatypes.generated.Uint256(_pky), 
                new org.web3j.abi.datatypes.generated.Uint256(_startTime), 
                new org.web3j.abi.datatypes.generated.Uint256(_endTime), 
                new org.web3j.abi.datatypes.Address(160, _tallyVerifier), 
                new org.web3j.abi.datatypes.Address(160, _electionAuthority), 
                new org.web3j.abi.datatypes.Address(160, _semaphore), 
                new org.web3j.abi.datatypes.generated.Uint256(_semaphoreGroupId)));
        return deployRemoteCall(Election.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Election> deploy(Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit, BigInteger _electionId, String _ipfsCid,
            byte[] _metadataHash, byte[] _merkleRoot, BigInteger _pkx, BigInteger _pky,
            BigInteger _startTime, BigInteger _endTime, String _tallyVerifier,
            String _electionAuthority, String _semaphore, BigInteger _semaphoreGroupId) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId), 
                new org.web3j.abi.datatypes.Utf8String(_ipfsCid), 
                new org.web3j.abi.datatypes.generated.Bytes32(_metadataHash), 
                new org.web3j.abi.datatypes.generated.Bytes32(_merkleRoot), 
                new org.web3j.abi.datatypes.generated.Uint256(_pkx), 
                new org.web3j.abi.datatypes.generated.Uint256(_pky), 
                new org.web3j.abi.datatypes.generated.Uint256(_startTime), 
                new org.web3j.abi.datatypes.generated.Uint256(_endTime), 
                new org.web3j.abi.datatypes.Address(160, _tallyVerifier), 
                new org.web3j.abi.datatypes.Address(160, _electionAuthority), 
                new org.web3j.abi.datatypes.Address(160, _semaphore), 
                new org.web3j.abi.datatypes.generated.Uint256(_semaphoreGroupId)));
        return deployRemoteCall(Election.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
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

    public static class SemaphoreProof extends StaticStruct {
        public BigInteger merkleTreeDepth;

        public BigInteger merkleTreeRoot;

        public BigInteger nullifier;

        public BigInteger message;

        public BigInteger scope;

        public List<BigInteger> points;

        public SemaphoreProof(BigInteger merkleTreeDepth, BigInteger merkleTreeRoot,
                BigInteger nullifier, BigInteger message, BigInteger scope,
                List<BigInteger> points) {
            super(new org.web3j.abi.datatypes.generated.Uint256(merkleTreeDepth), 
                    new org.web3j.abi.datatypes.generated.Uint256(merkleTreeRoot), 
                    new org.web3j.abi.datatypes.generated.Uint256(nullifier), 
                    new org.web3j.abi.datatypes.generated.Uint256(message), 
                    new org.web3j.abi.datatypes.generated.Uint256(scope), 
                    new org.web3j.abi.datatypes.generated.StaticArray8<org.web3j.abi.datatypes.generated.Uint256>(
                            org.web3j.abi.datatypes.generated.Uint256.class,
                            org.web3j.abi.Utils.typeMap(points, org.web3j.abi.datatypes.generated.Uint256.class)));
            this.merkleTreeDepth = merkleTreeDepth;
            this.merkleTreeRoot = merkleTreeRoot;
            this.nullifier = nullifier;
            this.message = message;
            this.scope = scope;
            this.points = points;
        }

        public SemaphoreProof(Uint256 merkleTreeDepth, Uint256 merkleTreeRoot, Uint256 nullifier,
                Uint256 message, Uint256 scope,
                @Parameterized(type = Uint256.class) StaticArray8<Uint256> points) {
            super(merkleTreeDepth, merkleTreeRoot, nullifier, message, scope, points);
            this.merkleTreeDepth = merkleTreeDepth.getValue();
            this.merkleTreeRoot = merkleTreeRoot.getValue();
            this.nullifier = nullifier.getValue();
            this.message = message.getValue();
            this.scope = scope.getValue();
            this.points = points.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
        }
    }

    public static class ElectionClosedEventResponse extends BaseEventResponse {
        public BigInteger electionId;

        public BigInteger timestamp;
    }

    public static class TallyPublishedEventResponse extends BaseEventResponse {
        public BigInteger electionId;

        public List<BigInteger> tallies;

        public String proofCid;
    }

    public static class VoteSubmittedEventResponse extends BaseEventResponse {
        public BigInteger electionId;

        public BigInteger voteIndex;

        public byte[] cipherHash;

        public BigInteger timestamp;
    }
}
