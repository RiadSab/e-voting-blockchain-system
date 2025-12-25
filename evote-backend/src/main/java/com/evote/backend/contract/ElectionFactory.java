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
    public static final String BINARY = "6080346100f657601f611a1138819003918201601f19168301916001600160401b038311848410176100fa5780849260409485528339810103126100f657610052602061004b8361010e565b920161010e565b6001600160a01b039091169081156100e3575f80546001600160a01b0319811684178255604051939182916001600160a01b0316907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09080a360018060a01b0319600354161760035560018060a01b031660018060a01b03196004541617600455600180556118ee90816101238239f35b631e4fbdf760e01b5f525f60045260245ffd5b5f80fd5b634e487b7160e01b5f52604160045260245ffd5b51906001600160a01b03821682036100f65756fe60806040526004361015610011575f80fd5b5f3560e01c80632776af8b1461064657806336266ee81461061e5780635e6fef0114610504578063715018a6146105c75780637b5d25341461059f5780638da5cb5b14610578578063997d28301461055b578063bd12dd6414610536578063dab50eb214610504578063f2fde38b1461047f5763f3ee406614610092575f80fd5b346102f85760e03660031901126102f85760043567ffffffffffffffff81116102f857366023820112156102f857806004013567ffffffffffffffff81116102f85736602482840101116102f85760243560843560a4359360c4359360018060a01b0385168095036102f8576003546001600160a01b0316330361042b5760045460ff8160a01c166103e65781156103a1578684101561035c5742871115610317576001549560018701808811610303576001556040516302e1f9db60e51b815230600482015291602090839060249082905f906001600160a01b03165af1918215610296575f926102b5575b50600354600454604051936111a592838601926001600160a01b03908116911667ffffffffffffffff8411878510176102a15761014095879561071487398c85528b6020860152604435604086015260643560608601528a60808601528d60a086015260c085015260e08401526101008301526101208201520301905ff095861561029657608086937f04d79b78a17f1d831bdecfa4ec28aee344c1b874ab5b3123259998a1a78a22169360249360209a60018060a01b031697875f5260028c5260405f20896bffffffffffffffffffffffff60a01b825416179055826040519687956060875282606088015201868601375f8484018601528b8401526040830152601f01601f19168101030190a4604051908152f35b6040513d5f823e3d90fd5b634e487b7160e01b5f52604160045260245ffd5b90915060203d6020116102fc575b601f8101601f1916820167ffffffffffffffff8111838210176102a1576020918391604052810103126102f85751905f61017f565b5f80fd5b503d6102c3565b634e487b7160e01b5f52601160045260245ffd5b60405162461bcd60e51b815260206004820152601860248201527f466163746f72793a20656e6454696d6520696e207061737400000000000000006044820152606490fd5b60405162461bcd60e51b815260206004820152601c60248201527f466163746f72793a20696e76616c69642074696d652077696e646f77000000006044820152606490fd5b60405162461bcd60e51b815260206004820152601960248201527f466163746f72793a2069706673436964207265717569726564000000000000006044820152606490fd5b60405162461bcd60e51b815260206004820152601860248201527f466163746f72793a206372656174696f6e2070617573656400000000000000006044820152606490fd5b60405162461bcd60e51b815260206004820152602660248201527f466163746f72793a2063616c6c6572206e6f7420656c6563746f72616c417574604482015265686f7269747960d01b6064820152608490fd5b346102f85760203660031901126102f8576104986106d7565b6104a06106ed565b6001600160a01b031680156104f1575f80546001600160a01b03198116831782556001600160a01b0316907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09080a3005b631e4fbdf760e01b5f525f60045260245ffd5b346102f85760203660031901126102f8576004355f526002602052602060018060a01b0360405f205416604051908152f35b346102f8575f3660031901126102f857602060ff60045460a01c166040519015158152f35b346102f8575f3660031901126102f8576020600154604051908152f35b346102f8575f3660031901126102f8575f546040516001600160a01b039091168152602090f35b346102f8575f3660031901126102f8576004546040516001600160a01b039091168152602090f35b346102f8575f3660031901126102f8576105df6106ed565b5f80546001600160a01b0319811682556001600160a01b03167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e08280a3005b346102f8575f3660031901126102f8576003546040516001600160a01b039091168152602090f35b346102f85760203660031901126102f85761065f6106d7565b6106676106ed565b6001600160a01b03168015610692576bffffffffffffffffffffffff60a01b60035416176003555f80f35b60405162461bcd60e51b815260206004820152601f60248201527f466163746f72793a20617574686f72697479207a65726f2061646472657373006044820152606490fd5b600435906001600160a01b03821682036102f857565b5f546001600160a01b0316330361070057565b63118cdaa760e01b5f523360045260245ffdfe60c03461018e57601f6111a538819003918201601f19168301916001600160401b03831184841017610192578084926101409460405283398101031261018e57805190602081015160408201516060830151608084015160a08501519161006860c087016101a6565b9361007560e088016101a6565b956101206100866101008a016101a6565b9801519860017f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f0055858510156101495760805260a0525f55600155600255600355600480546001600160a01b03199081166001600160a01b0393841617909155600580548216938316939093179092556006805490921692169190911790556007556008805461ffff19169055604051610fea90816101bb8239608051818181610347015281816108c101528181610ad50152610e32015260a051816106050152f35b60405162461bcd60e51b815260206004820152601460248201527f456c656374696f6e3a206261642077696e646f770000000000000000000000006044820152606490fd5b5f80fd5b634e487b7160e01b5f52604160045260245ffd5b51906001600160a01b038216820361018e5756fe6080806040526004361015610012575f80fd5b5f905f3560e01c908163051364d414610e1d5750806311fbf2c114610e00578063258360b014610de35780633197cbb614610dc65780633a5f547a14610d8957806346401ed214610d645780634824b7e81461094b578063597e1fb5146109295780636c6c32d014610868578063722f4a1f1461084b57806378e979251461082e5780637b5d253414610806578063a13f2f8d14610747578063a2f5242214610651578063aa7f086914610628578063c5a1d7f0146105ed578063d32642b91461029b578063d6717a43146101c4578063d729069d14610176578063d7a6f6e814610158578063dc46d6ea1461012f5763fa6df55d14610110575f80fd5b3461012c578060031936011261012c5760209054604051908152f35b80fd5b503461012c578060031936011261012c576005546040516001600160a01b039091168152602090f35b503461012c578060031936011261012c576020600154604051908152f35b503461012c57602036600319011261012c576004356001600160a01b038116908190036101c0576101a5610f1b565b6bffffffffffffffffffffffff60a01b600454161760045580f35b5080fd5b503461012c578060031936011261012c57604051908091600a546101e781610e55565b808352602083019160018116908115610280575060011461023d575b5061021382604094950383610e8d565b8251938492602084525180928160208601528585015e828201840152601f01601f19168101030190f35b600a84529350825f516020610f755f395f51905f525b85821061026a575082016020019350610213610203565b6001816020925483858801015201910190610253565b60ff1916835250151560051b82016020019350610213610203565b503461012c5761022036600319011261012c576004356044356024356064356101a03660831901126104a85760027f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f0054146105de5760027f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f005560025442106105a15760035442116105675760085460ff811661052f5760ff6103419160081c1615610eaf565b604080517f000000000000000000000000000000000000000000000000000000000000000060208201818152928201879052606082018590526080820186905260a08083018590528252919061039860c082610e8d565b5190209060e4359182036104f257610104359081036104b757600654600754889390916001600160a01b0316803b156104a8578492836101c492604051968795869463d0d898dd60e01b86526004860152608435602486015260a435604486015260c4356064860152608485015260a484015261010061012460c48501375af180156104ac57610493575b50506040805194855260208501929092529083019190915260608201524260808201527f2600691e00c1e8db90cc9573f335c44e04be59e3bd4c63134f1c90d3e1fad1e29060a090a160017f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f005580f35b8161049d91610e8d565b6104a857845f610423565b8480fd5b6040513d84823e3d90fd5b60405162461bcd60e51b8152602060048201526013602482015272456c656374696f6e3a206261642073636f706560681b6044820152606490fd5b60405162461bcd60e51b8152602060048201526015602482015274456c656374696f6e3a20626164206d65737361676560581b6044820152606490fd5b60405162461bcd60e51b815260206004820152601060248201526f115b1958dd1a5bdb8e8818db1bdcd95960821b6044820152606490fd5b60405162461bcd60e51b8152602060048201526012602482015271115b1958dd1a5bdb8e88199a5b9a5cda195960721b6044820152606490fd5b60405162461bcd60e51b8152602060048201526015602482015274115b1958dd1a5bdb8e881b9bdd081cdd185c9d1959605a1b6044820152606490fd5b633ee5aeb560e01b8552600485fd5b503461012c578060031936011261012c5760206040517f00000000000000000000000000000000000000000000000000000000000000008152f35b503461012c578060031936011261012c576004546040516001600160a01b039091168152602090f35b503461012c578060031936011261012c5760ff60085460081c16156107025760405180602060095491828152018091600985525f516020610f955f395f51905f5290855b8181106106ec57505050826106ab910383610e8d565b604051928392602084019060208552518091526040840192915b8181106106d3575050500390f35b82518452859450602093840193909201916001016106c5565b8254845260209093019260019283019201610695565b60405162461bcd60e51b815260206004820152601d60248201527f456c656374696f6e3a2074616c6c79206e6f74207075626c69736865640000006044820152606490fd5b503461080257602036600319011261080257600435610764610f1b565b60018060a01b0360065416600754813b15610802575f91604483926040519485938492631783efc360e01b845260048401528760248401525af180156107f7576107e2575b507ff45b8429c36d478f9a6d081c4811a819b98aa5b608588bed9d406405cf282473602060018060a01b0360055416604051908152a280f35b6107ef9192505f90610e8d565b5f905f6107a9565b6040513d5f823e3d90fd5b5f80fd5b34610802575f366003190112610802576006546040516001600160a01b039091168152602090f35b34610802575f366003190112610802576020600254604051908152f35b34610802575f366003190112610802576020600754604051908152f35b34610802575f36600319011261080257610880610f1b565b60085460ff81166108e45760019060ff1916176008556040514281527fd3461d57d954b89a789e6fbd29ff23c87fef073eae45cc372d7e26cb1459435360207f000000000000000000000000000000000000000000000000000000000000000092a2005b60405162461bcd60e51b815260206004820152601860248201527f456c656374696f6e3a20616c726561647920636c6f73656400000000000000006044820152606490fd5b34610802575f36600319011261080257602060ff600854166040519015158152f35b346108025760403660031901126108025760043567ffffffffffffffff8111610802573660238201121561080257806004013567ffffffffffffffff81116108025760248201918160051b906024823692010111610802576024359267ffffffffffffffff841161080257366023850112156108025783600401359167ffffffffffffffff83116108025760248501943660248583010111610802576004546001600160a01b03163303610d145760ff600854610a0d828260081c1615610eaf565b168015610d09575b15610cb057680100000000000000008511610c9c5760095485600955808610610c6e575b5083908360095f525f5b878110610c4a575050610a57600a54610e55565b601f8111610bec575b505f90601f8311600114610b74575f92610b66575b50508360011b905f198560031b1c191617600a555b601f19601f840116604051610aa26020830182610e8d565b8481526020810190858883375f60208783010152519020600b5561010061ff0019600854161760085542600c55604051947f000000000000000000000000000000000000000000000000000000000000000086526060602087015280606087015260018060fb1b0310610802577f15a054831771b8c6c68ce9eab7781b5645284a01ec828b20970d0c0f5aed5df5955f602060808782988783988c9a858c0137890195838a88030160408b0152818488015260a087013784010101520101030190a1005b602492500101358680610a75565b909150601f19851691600a5f52855f516020610f755f395f51905f52935f5b818110610bce575010610bb2575b505050600183811b01600a55610a8a565b01602401355f19600386901b60f8161c19169055858080610ba1565b84840160240135865560019095019460209384019389935001610b93565b610c2f90600a5f52601f840160051c5f516020610f755f395f51905f52019060208510610c35575b601f0160051c5f516020610f755f395f51905f520190610f05565b87610a60565b5f516020610f755f395f51905f529150610c14565b81355f516020610f955f395f51905f52820155869350602090910190600101610a43565b610c96905f516020610f955f395f51905f5201865f516020610f955f395f51905f5201610f05565b86610a39565b634e487b7160e01b5f52604160045260245ffd5b60405162461bcd60e51b815260206004820152602b60248201527f456c656374696f6e3a2063616e6e6f74207075626c6973682074616c6c79206260448201526a65666f726520636c6f736560a81b6064820152608490fd5b506003544211610a15565b60405162461bcd60e51b815260206004820152602260248201527f456c656374696f6e3a2063616c6c6572206e6f742074616c6c7956657269666960448201526132b960f11b6064820152608490fd5b34610802575f36600319011261080257602060ff60085460081c166040519015158152f35b34610802576020366003190112610802576004356009548110156108025760209060095f525f516020610f955f395f51905f520154604051908152f35b34610802575f366003190112610802576020600354604051908152f35b34610802575f366003190112610802576020600b54604051908152f35b34610802575f366003190112610802576020600c54604051908152f35b34610802575f366003190112610802576020907f00000000000000000000000000000000000000000000000000000000000000008152f35b90600182811c92168015610e83575b6020831014610e6f57565b634e487b7160e01b5f52602260045260245ffd5b91607f1691610e64565b90601f8019910116810190811067ffffffffffffffff821117610c9c57604052565b15610eb657565b60405162461bcd60e51b815260206004820152602160248201527f456c656374696f6e3a2074616c6c7920616c7265616479207075626c697368656044820152601960fa1b6064820152608490fd5b818110610f10575050565b5f8155600101610f05565b6005546001600160a01b03163303610f2f57565b60405162461bcd60e51b815260206004820152601e60248201527f456c656374696f6e3a2063616c6c6572206e6f7420617574686f7269747900006044820152606490fdfec65a7bb8d6351c1cf70c95a316cc6a92839c986682d98bc35f958f4883f9d2a86e1540171b6c0c960b71a7020d9f60077f6af931a8bbf590da0223dacf75c7afa2646970667358221220a4e9f8b682704b3b1827f23189fe5c4cda0042460d3798d063282233b364e0a164736f6c634300081c0033a2646970667358221220d797b654d5bdea1ea33b184a0110b0d82548af17ced05571abe7bd2ecc2ffa9a64736f6c634300081c0033\n";

    private static String librariesLinkedBinary;

    public static final String FUNC_CREATEELECTION = "createElection";

    public static final String FUNC_CREATIONPAUSED = "creationPaused";

    public static final String FUNC_ELECTIONCOUNT = "electionCount";

    public static final String FUNC_ELECTIONS = "elections";

    public static final String FUNC_ELECTORALAUTHORITY = "electoralAuthority";

    public static final String FUNC_GETELECTIONADDRESS = "getElectionAddress";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SEMAPHORE = "semaphore";

    public static final String FUNC_SETELECTORALAUTHORITY = "setElectoralAuthority";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event ELECTIONCREATED_EVENT = new Event("ElectionCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Bytes32>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event FACTORYPAUSED_EVENT = new Event("FactoryPaused", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
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

    public RemoteFunctionCall<TransactionReceipt> createElection(String ipfsCid,
            byte[] metadataHash, BigInteger pkx, BigInteger pky, BigInteger startTime,
            BigInteger endTime, String tallyVerifier) {
        final Function function = new Function(
                FUNC_CREATEELECTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ipfsCid), 
                new org.web3j.abi.datatypes.generated.Bytes32(metadataHash), 
                new org.web3j.abi.datatypes.generated.Uint256(pkx), 
                new org.web3j.abi.datatypes.generated.Uint256(pky), 
                new org.web3j.abi.datatypes.generated.Uint256(startTime), 
                new org.web3j.abi.datatypes.generated.Uint256(endTime), 
                new org.web3j.abi.datatypes.Address(160, tallyVerifier)), 
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

    public RemoteFunctionCall<String> semaphore() {
        final Function function = new Function(FUNC_SEMAPHORE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setElectoralAuthority(String _authority) {
        final Function function = new Function(
                FUNC_SETELECTORALAUTHORITY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _authority)), 
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
            String _semaphore) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _electoralAuthority), 
                new org.web3j.abi.datatypes.Address(160, _semaphore)));
        return deployRemoteCall(ElectionFactory.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<ElectionFactory> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider,
            String _electoralAuthority, String _semaphore) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _electoralAuthority), 
                new org.web3j.abi.datatypes.Address(160, _semaphore)));
        return deployRemoteCall(ElectionFactory.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<ElectionFactory> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit, String _electoralAuthority,
            String _semaphore) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _electoralAuthority), 
                new org.web3j.abi.datatypes.Address(160, _semaphore)));
        return deployRemoteCall(ElectionFactory.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<ElectionFactory> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,
            String _electoralAuthority, String _semaphore) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _electoralAuthority), 
                new org.web3j.abi.datatypes.Address(160, _semaphore)));
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

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }
}
