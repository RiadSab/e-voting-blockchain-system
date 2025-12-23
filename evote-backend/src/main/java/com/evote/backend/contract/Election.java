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
    public static final String BINARY = "60c03461018e57601f61110438819003918201601f19168301916001600160401b03831184841017610192578084926101409460405283398101031261018e57805190602081015160408201516060830151608084015160a08501519161006860c087016101a6565b9361007560e088016101a6565b956101206100866101008a016101a6565b9801519860017f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f0055858510156101495760805260a0525f55600155600255600355600480546001600160a01b03199081166001600160a01b0393841617909155600580548216938316939093179092556006805490921692169190911790556007556008805461ffff19169055604051610f4990816101bb823960805181818161033d0152818161082001528181610a340152610d91015260a051816106240152f35b60405162461bcd60e51b815260206004820152601460248201527f456c656374696f6e3a206261642077696e646f770000000000000000000000006044820152606490fd5b5f80fd5b634e487b7160e01b5f52604160045260245ffd5b51906001600160a01b038216820361018e5756fe6080806040526004361015610012575f80fd5b5f905f3560e01c908163051364d414610d7c5750806311fbf2c114610d5f578063258360b014610d425780633197cbb614610d255780633a5f547a14610ce857806346401ed214610cc35780634824b7e8146108aa578063597e1fb5146108885780636c6c32d0146107c7578063722f4a1f146107aa57806378e979251461078d5780637b5d253414610765578063a2f524221461066f578063aa7f086914610647578063c5a1d7f01461060d578063d32642b914610290578063d6717a43146101b9578063d729069d1461016b578063d7a6f6e81461014d578063dc46d6ea146101245763fa6df55d14610105575f80fd5b3461012157806003193601126101215760209054604051908152f35b80fd5b50346101215780600319360112610121576005546040516001600160a01b039091168152602090f35b50346101215780600319360112610121576020600154604051908152f35b5034610121576020366003190112610121576004356001600160a01b038116908190036101b55761019a610e7a565b6bffffffffffffffffffffffff60a01b600454161760045580f35b5080fd5b5034610121578060031936011261012157604051908091600a546101dc81610db4565b8083526020830191600181169081156102755750600114610232575b5061020882604094950383610dec565b8251938492602084525180928160208601528585015e828201840152601f01601f19168101030190f35b600a84529350825f516020610ed45f395f51905f525b85821061025f5750820160200193506102086101f8565b6001816020925483858801015201910190610248565b60ff1916835250151560051b820160200193506102086101f8565b50346104d3576102203660031901126104d357606435906044356024356004356101a03660831901126104d35760027f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f0054146105fe5760027f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f005560025442106105c15760035442116105875760085460ff811661054f5760ff6103379160081c1615610e0e565b604080517f000000000000000000000000000000000000000000000000000000000000000060208201818152928201849052606082018590526080820186905260a08083018990528252919061038e60c082610dec565b51902060e43590810361051257610104359182036104d75760065460075491906001600160a01b0316803b156104d3575f92836101c492604051968795869463d0d898dd60e01b86526004860152608435602486015260a435604486015260c4356064860152608485015260a484015261010061012460c48501375af180156104c85761048e575b50604080519182526020820192909252908101919091526060810192909252426080830152907f2600691e00c1e8db90cc9573f335c44e04be59e3bd4c63134f1c90d3e1fad1e2908060a081015b0390a160017f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f005580f35b7f2600691e00c1e8db90cc9573f335c44e04be59e3bd4c63134f1c90d3e1fad1e29450906104bf5f61046493610dec565b5f945090610416565b6040513d5f823e3d90fd5b5f80fd5b60405162461bcd60e51b8152602060048201526013602482015272456c656374696f6e3a206261642073636f706560681b6044820152606490fd5b60405162461bcd60e51b8152602060048201526015602482015274456c656374696f6e3a20626164206d65737361676560581b6044820152606490fd5b60405162461bcd60e51b815260206004820152601060248201526f115b1958dd1a5bdb8e8818db1bdcd95960821b6044820152606490fd5b60405162461bcd60e51b8152602060048201526012602482015271115b1958dd1a5bdb8e88199a5b9a5cda195960721b6044820152606490fd5b60405162461bcd60e51b8152602060048201526015602482015274115b1958dd1a5bdb8e881b9bdd081cdd185c9d1959605a1b6044820152606490fd5b633ee5aeb560e01b5f5260045ffd5b346104d3575f3660031901126104d35760206040517f00000000000000000000000000000000000000000000000000000000000000008152f35b346104d3575f3660031901126104d3576004546040516001600160a01b039091168152602090f35b346104d3575f3660031901126104d35760ff60085460081c1615610720576040518060206009549283815201809260095f525f516020610ef45f395f51905f52905f5b81811061070a57505050816106c8910382610dec565b604051918291602083019060208452518091526040830191905f5b8181106106f1575050500390f35b82518452859450602093840193909201916001016106e3565b82548452602090930192600192830192016106b2565b60405162461bcd60e51b815260206004820152601d60248201527f456c656374696f6e3a2074616c6c79206e6f74207075626c69736865640000006044820152606490fd5b346104d3575f3660031901126104d3576006546040516001600160a01b039091168152602090f35b346104d3575f3660031901126104d3576020600254604051908152f35b346104d3575f3660031901126104d3576020600754604051908152f35b346104d3575f3660031901126104d3576107df610e7a565b60085460ff81166108435760019060ff1916176008556040514281527fd3461d57d954b89a789e6fbd29ff23c87fef073eae45cc372d7e26cb1459435360207f000000000000000000000000000000000000000000000000000000000000000092a2005b60405162461bcd60e51b815260206004820152601860248201527f456c656374696f6e3a20616c726561647920636c6f73656400000000000000006044820152606490fd5b346104d3575f3660031901126104d357602060ff600854166040519015158152f35b346104d35760403660031901126104d35760043567ffffffffffffffff81116104d357366023820112156104d357806004013567ffffffffffffffff81116104d35760248201918160051b9060248236920101116104d3576024359267ffffffffffffffff84116104d357366023850112156104d35783600401359167ffffffffffffffff83116104d357602485019436602485830101116104d3576004546001600160a01b03163303610c735760ff60085461096c828260081c1615610e0e565b168015610c68575b15610c0f57680100000000000000008511610bfb5760095485600955808610610bcd575b5083908360095f525f5b878110610ba95750506109b6600a54610db4565b601f8111610b4b575b505f90601f8311600114610ad3575f92610ac5575b50508360011b905f198560031b1c191617600a555b601f19601f840116604051610a016020830182610dec565b8481526020810190858883375f60208783010152519020600b5561010061ff0019600854161760085542600c55604051947f000000000000000000000000000000000000000000000000000000000000000086526060602087015280606087015260018060fb1b03106104d3577f15a054831771b8c6c68ce9eab7781b5645284a01ec828b20970d0c0f5aed5df5955f602060808782988783988c9a858c0137890195838a88030160408b0152818488015260a087013784010101520101030190a1005b6024925001013586806109d4565b909150601f19851691600a5f52855f516020610ed45f395f51905f52935f5b818110610b2d575010610b11575b505050600183811b01600a556109e9565b01602401355f19600386901b60f8161c19169055858080610b00565b84840160240135865560019095019460209384019389935001610af2565b610b8e90600a5f52601f840160051c5f516020610ed45f395f51905f52019060208510610b94575b601f0160051c5f516020610ed45f395f51905f520190610e64565b876109bf565b5f516020610ed45f395f51905f529150610b73565b81355f516020610ef45f395f51905f528201558693506020909101906001016109a2565b610bf5905f516020610ef45f395f51905f5201865f516020610ef45f395f51905f5201610e64565b86610998565b634e487b7160e01b5f52604160045260245ffd5b60405162461bcd60e51b815260206004820152602b60248201527f456c656374696f6e3a2063616e6e6f74207075626c6973682074616c6c79206260448201526a65666f726520636c6f736560a81b6064820152608490fd5b506003544211610974565b60405162461bcd60e51b815260206004820152602260248201527f456c656374696f6e3a2063616c6c6572206e6f742074616c6c7956657269666960448201526132b960f11b6064820152608490fd5b346104d3575f3660031901126104d357602060ff60085460081c166040519015158152f35b346104d35760203660031901126104d3576004356009548110156104d35760209060095f525f516020610ef45f395f51905f520154604051908152f35b346104d3575f3660031901126104d3576020600354604051908152f35b346104d3575f3660031901126104d3576020600b54604051908152f35b346104d3575f3660031901126104d3576020600c54604051908152f35b346104d3575f3660031901126104d3576020907f00000000000000000000000000000000000000000000000000000000000000008152f35b90600182811c92168015610de2575b6020831014610dce57565b634e487b7160e01b5f52602260045260245ffd5b91607f1691610dc3565b90601f8019910116810190811067ffffffffffffffff821117610bfb57604052565b15610e1557565b60405162461bcd60e51b815260206004820152602160248201527f456c656374696f6e3a2074616c6c7920616c7265616479207075626c697368656044820152601960fa1b6064820152608490fd5b818110610e6f575050565b5f8155600101610e64565b6005546001600160a01b03163303610e8e57565b60405162461bcd60e51b815260206004820152601e60248201527f456c656374696f6e3a2063616c6c6572206e6f7420617574686f7269747900006044820152606490fdfec65a7bb8d6351c1cf70c95a316cc6a92839c986682d98bc35f958f4883f9d2a86e1540171b6c0c960b71a7020d9f60077f6af931a8bbf590da0223dacf75c7afa2646970667358221220f16e0563c7cfd058fb2ec835216bfe8050197c08a298a718deb712451ad1f1fb64736f6c634300081c0033\n";

    private static String librariesLinkedBinary;

    public static final String FUNC_CLOSEELECTION = "closeElection";

    public static final String FUNC_CLOSED = "closed";

    public static final String FUNC_ELECTIONAUTHORITY = "electionAuthority";

    public static final String FUNC_ELECTIONID = "electionId";

    public static final String FUNC_ENDTIME = "endTime";

    public static final String FUNC_FINALTALLY = "finalTally";

    public static final String FUNC_FINALTALLYPUBLISHED = "finalTallyPublished";

    public static final String FUNC_GETFINALTALLY = "getFinalTally";

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
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event VOTESUBMITTED_EVENT = new Event("VoteSubmitted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
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
            typedResponse.electionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tallies = (List<BigInteger>) ((Array) eventValues.getNonIndexedValues().get(1)).getNativeValueCopy();
            typedResponse.proofCid = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TallyPublishedEventResponse getTallyPublishedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TALLYPUBLISHED_EVENT, log);
        TallyPublishedEventResponse typedResponse = new TallyPublishedEventResponse();
        typedResponse.log = log;
        typedResponse.electionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.tallies = (List<BigInteger>) ((Array) eventValues.getNonIndexedValues().get(1)).getNativeValueCopy();
        typedResponse.proofCid = (String) eventValues.getNonIndexedValues().get(2).getValue();
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
            typedResponse.c1x = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.c1y = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.c2x = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.c2y = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static VoteSubmittedEventResponse getVoteSubmittedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(VOTESUBMITTED_EVENT, log);
        VoteSubmittedEventResponse typedResponse = new VoteSubmittedEventResponse();
        typedResponse.log = log;
        typedResponse.c1x = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.c1y = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.c2x = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.c2y = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
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
            ContractGasProvider contractGasProvider, BigInteger _electionId, byte[] _metadataHash,
            BigInteger _pkx, BigInteger _pky, BigInteger _startTime, BigInteger _endTime,
            String _tallyVerifier, String _electionAuthority, String _semaphore,
            BigInteger _semaphoreGroupId) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId), 
                new org.web3j.abi.datatypes.generated.Bytes32(_metadataHash), 
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
            ContractGasProvider contractGasProvider, BigInteger _electionId, byte[] _metadataHash,
            BigInteger _pkx, BigInteger _pky, BigInteger _startTime, BigInteger _endTime,
            String _tallyVerifier, String _electionAuthority, String _semaphore,
            BigInteger _semaphoreGroupId) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId), 
                new org.web3j.abi.datatypes.generated.Bytes32(_metadataHash), 
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
            BigInteger gasPrice, BigInteger gasLimit, BigInteger _electionId, byte[] _metadataHash,
            BigInteger _pkx, BigInteger _pky, BigInteger _startTime, BigInteger _endTime,
            String _tallyVerifier, String _electionAuthority, String _semaphore,
            BigInteger _semaphoreGroupId) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId), 
                new org.web3j.abi.datatypes.generated.Bytes32(_metadataHash), 
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
            BigInteger gasPrice, BigInteger gasLimit, BigInteger _electionId, byte[] _metadataHash,
            BigInteger _pkx, BigInteger _pky, BigInteger _startTime, BigInteger _endTime,
            String _tallyVerifier, String _electionAuthority, String _semaphore,
            BigInteger _semaphoreGroupId) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_electionId), 
                new org.web3j.abi.datatypes.generated.Bytes32(_metadataHash), 
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
        public BigInteger c1x;

        public BigInteger c1y;

        public BigInteger c2x;

        public BigInteger c2y;

        public BigInteger timestamp;
    }
}
