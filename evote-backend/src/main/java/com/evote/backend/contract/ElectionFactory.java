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
    public static final String BINARY = "60806040523461013257604051601f611a3138819003918201601f19168301916001600160401b0383118484101761010b57808492604094855283398101031261013257610058602061005183610136565b9201610136565b906001600160a01b0316801561011f575f80546001600160a01b031981168317825582916001600160a01b03909116907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09080a3600380546001600160a01b03191691909117905560405190602082016001600160401b0381118382101761010b5760405260018060a01b031680915260018060a01b03196004541617600455600180556040516118e6908161014b8239f35b634e487b7160e01b5f52604160045260245ffd5b631e4fbdf760e01b5f525f60045260245ffd5b5f80fd5b51906001600160a01b03821682036101325756fe60806040526004361015610011575f80fd5b5f3560e01c80632776af8b146106df57806336266ee8146106b75780635e6fef0114610131578063715018a6146106605780637cdfbc2f146102cc5780638da5cb5b146102a5578063997d2830146102885780639d194cf01461021c578063a21d131c146101b0578063a7c1abe014610188578063bd12dd6414610163578063dab50eb2146101315763f2fde38b146100a8575f80fd5b3461012d57602036600319011261012d576100c1610770565b6100c9610786565b6001600160a01b0316801561011a575f80546001600160a01b03198116831782556001600160a01b0316907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09080a3005b631e4fbdf760e01b5f525f60045260245ffd5b5f80fd5b3461012d57602036600319011261012d576004355f526002602052602060018060a01b0360405f205416604051908152f35b3461012d575f36600319011261012d57602060ff60035460a01c166040519015158152f35b3461012d575f36600319011261012d576004546040516001600160a01b039091168152602090f35b3461012d57602036600319011261012d5760043580151580910361012d5760207ffce805cba192a0e94e99b9309e2e9e2d36cd03bdc97ff6fe47fa5c2ea374d058916101fa610786565b6003805460ff60a01b191660a083901b60ff60a01b16179055604051908152a1005b3461012d57602036600319011261012d577f799652137a2d451f5b118f648da344343fe18f7c40bf1d528cc7defdf8e009516020610258610770565b610260610786565b600480546001600160a01b0319166001600160a01b03929092169182179055604051908152a1005b3461012d575f36600319011261012d576020600154604051908152f35b3461012d575f36600319011261012d575f546040516001600160a01b039091168152602090f35b3461012d5761012036600319011261012d5760043567ffffffffffffffff811161012d573660238201121561012d57806004013567ffffffffffffffff811161012d57366024828401011161012d5760a4359160c4359160243591608435916001600160a01b03851680860361012d5760e4356001600160a01b038116969087900361012d576003546001600160a01b03811692903384900361060c5760a01c60ff166105c7578315610582578886101561053d57428911156104f8576104f357506004546001600160a01b03165b60015496600188018089116104df5760015560405192611104918285019185831067ffffffffffffffff8411176104cb576101409486946107ad86398b8452602084018b905260443560408501526064356060850152608084018a905260a084018d90526001600160a01b031660c084015260e0830152610100820152610104356101208201520301905ff09586156104c057608086937f04d79b78a17f1d831bdecfa4ec28aee344c1b874ab5b3123259998a1a78a22169360249360209a60018060a01b031697875f5260028c5260405f20896bffffffffffffffffffffffff60a01b825416179055826040519687956060875282606088015201868601375f8484018601528b8401526040830152601f01601f19168101030190a4604051908152f35b6040513d5f823e3d90fd5b634e487b7160e01b5f52604160045260245ffd5b634e487b7160e01b5f52601160045260245ffd5b61039b565b60405162461bcd60e51b815260206004820152601860248201527f466163746f72793a20656e6454696d6520696e207061737400000000000000006044820152606490fd5b60405162461bcd60e51b815260206004820152601c60248201527f466163746f72793a20696e76616c69642074696d652077696e646f77000000006044820152606490fd5b60405162461bcd60e51b815260206004820152601960248201527f466163746f72793a2069706673436964207265717569726564000000000000006044820152606490fd5b60405162461bcd60e51b815260206004820152601860248201527f466163746f72793a206372656174696f6e2070617573656400000000000000006044820152606490fd5b60405162461bcd60e51b815260206004820152602660248201527f466163746f72793a2063616c6c6572206e6f7420656c6563746f72616c417574604482015265686f7269747960d01b6064820152608490fd5b3461012d575f36600319011261012d57610678610786565b5f80546001600160a01b0319811682556001600160a01b03167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e08280a3005b3461012d575f36600319011261012d576003546040516001600160a01b039091168152602090f35b3461012d57602036600319011261012d576106f8610770565b610700610786565b6001600160a01b0316801561072b576bffffffffffffffffffffffff60a01b60035416176003555f80f35b60405162461bcd60e51b815260206004820152601f60248201527f466163746f72793a20617574686f72697479207a65726f2061646472657373006044820152606490fd5b600435906001600160a01b038216820361012d57565b5f546001600160a01b0316330361079957565b63118cdaa760e01b5f523360045260245ffdfe60c03461018e57601f61110438819003918201601f19168301916001600160401b03831184841017610192578084926101409460405283398101031261018e57805190602081015160408201516060830151608084015160a08501519161006860c087016101a6565b9361007560e088016101a6565b956101206100866101008a016101a6565b9801519860017f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f0055858510156101495760805260a0525f55600155600255600355600480546001600160a01b03199081166001600160a01b0393841617909155600580548216938316939093179092556006805490921692169190911790556007556008805461ffff19169055604051610f4990816101bb823960805181818161033d0152818161082001528181610a340152610d91015260a051816106240152f35b60405162461bcd60e51b815260206004820152601460248201527f456c656374696f6e3a206261642077696e646f770000000000000000000000006044820152606490fd5b5f80fd5b634e487b7160e01b5f52604160045260245ffd5b51906001600160a01b038216820361018e5756fe6080806040526004361015610012575f80fd5b5f905f3560e01c908163051364d414610d7c5750806311fbf2c114610d5f578063258360b014610d425780633197cbb614610d255780633a5f547a14610ce857806346401ed214610cc35780634824b7e8146108aa578063597e1fb5146108885780636c6c32d0146107c7578063722f4a1f146107aa57806378e979251461078d5780637b5d253414610765578063a2f524221461066f578063aa7f086914610647578063c5a1d7f01461060d578063d32642b914610290578063d6717a43146101b9578063d729069d1461016b578063d7a6f6e81461014d578063dc46d6ea146101245763fa6df55d14610105575f80fd5b3461012157806003193601126101215760209054604051908152f35b80fd5b50346101215780600319360112610121576005546040516001600160a01b039091168152602090f35b50346101215780600319360112610121576020600154604051908152f35b5034610121576020366003190112610121576004356001600160a01b038116908190036101b55761019a610e7a565b6bffffffffffffffffffffffff60a01b600454161760045580f35b5080fd5b5034610121578060031936011261012157604051908091600a546101dc81610db4565b8083526020830191600181169081156102755750600114610232575b5061020882604094950383610dec565b8251938492602084525180928160208601528585015e828201840152601f01601f19168101030190f35b600a84529350825f516020610ed45f395f51905f525b85821061025f5750820160200193506102086101f8565b6001816020925483858801015201910190610248565b60ff1916835250151560051b820160200193506102086101f8565b50346104d3576102203660031901126104d357606435906044356024356004356101a03660831901126104d35760027f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f0054146105fe5760027f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f005560025442106105c15760035442116105875760085460ff811661054f5760ff6103379160081c1615610e0e565b604080517f000000000000000000000000000000000000000000000000000000000000000060208201818152928201849052606082018590526080820186905260a08083018990528252919061038e60c082610dec565b51902060e43590810361051257610104359182036104d75760065460075491906001600160a01b0316803b156104d3575f92836101c492604051968795869463d0d898dd60e01b86526004860152608435602486015260a435604486015260c4356064860152608485015260a484015261010061012460c48501375af180156104c85761048e575b50604080519182526020820192909252908101919091526060810192909252426080830152907f2600691e00c1e8db90cc9573f335c44e04be59e3bd4c63134f1c90d3e1fad1e2908060a081015b0390a160017f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f005580f35b7f2600691e00c1e8db90cc9573f335c44e04be59e3bd4c63134f1c90d3e1fad1e29450906104bf5f61046493610dec565b5f945090610416565b6040513d5f823e3d90fd5b5f80fd5b60405162461bcd60e51b8152602060048201526013602482015272456c656374696f6e3a206261642073636f706560681b6044820152606490fd5b60405162461bcd60e51b8152602060048201526015602482015274456c656374696f6e3a20626164206d65737361676560581b6044820152606490fd5b60405162461bcd60e51b815260206004820152601060248201526f115b1958dd1a5bdb8e8818db1bdcd95960821b6044820152606490fd5b60405162461bcd60e51b8152602060048201526012602482015271115b1958dd1a5bdb8e88199a5b9a5cda195960721b6044820152606490fd5b60405162461bcd60e51b8152602060048201526015602482015274115b1958dd1a5bdb8e881b9bdd081cdd185c9d1959605a1b6044820152606490fd5b633ee5aeb560e01b5f5260045ffd5b346104d3575f3660031901126104d35760206040517f00000000000000000000000000000000000000000000000000000000000000008152f35b346104d3575f3660031901126104d3576004546040516001600160a01b039091168152602090f35b346104d3575f3660031901126104d35760ff60085460081c1615610720576040518060206009549283815201809260095f525f516020610ef45f395f51905f52905f5b81811061070a57505050816106c8910382610dec565b604051918291602083019060208452518091526040830191905f5b8181106106f1575050500390f35b82518452859450602093840193909201916001016106e3565b82548452602090930192600192830192016106b2565b60405162461bcd60e51b815260206004820152601d60248201527f456c656374696f6e3a2074616c6c79206e6f74207075626c69736865640000006044820152606490fd5b346104d3575f3660031901126104d3576006546040516001600160a01b039091168152602090f35b346104d3575f3660031901126104d3576020600254604051908152f35b346104d3575f3660031901126104d3576020600754604051908152f35b346104d3575f3660031901126104d3576107df610e7a565b60085460ff81166108435760019060ff1916176008556040514281527fd3461d57d954b89a789e6fbd29ff23c87fef073eae45cc372d7e26cb1459435360207f000000000000000000000000000000000000000000000000000000000000000092a2005b60405162461bcd60e51b815260206004820152601860248201527f456c656374696f6e3a20616c726561647920636c6f73656400000000000000006044820152606490fd5b346104d3575f3660031901126104d357602060ff600854166040519015158152f35b346104d35760403660031901126104d35760043567ffffffffffffffff81116104d357366023820112156104d357806004013567ffffffffffffffff81116104d35760248201918160051b9060248236920101116104d3576024359267ffffffffffffffff84116104d357366023850112156104d35783600401359167ffffffffffffffff83116104d357602485019436602485830101116104d3576004546001600160a01b03163303610c735760ff60085461096c828260081c1615610e0e565b168015610c68575b15610c0f57680100000000000000008511610bfb5760095485600955808610610bcd575b5083908360095f525f5b878110610ba95750506109b6600a54610db4565b601f8111610b4b575b505f90601f8311600114610ad3575f92610ac5575b50508360011b905f198560031b1c191617600a555b601f19601f840116604051610a016020830182610dec565b8481526020810190858883375f60208783010152519020600b5561010061ff0019600854161760085542600c55604051947f000000000000000000000000000000000000000000000000000000000000000086526060602087015280606087015260018060fb1b03106104d3577f15a054831771b8c6c68ce9eab7781b5645284a01ec828b20970d0c0f5aed5df5955f602060808782988783988c9a858c0137890195838a88030160408b0152818488015260a087013784010101520101030190a1005b6024925001013586806109d4565b909150601f19851691600a5f52855f516020610ed45f395f51905f52935f5b818110610b2d575010610b11575b505050600183811b01600a556109e9565b01602401355f19600386901b60f8161c19169055858080610b00565b84840160240135865560019095019460209384019389935001610af2565b610b8e90600a5f52601f840160051c5f516020610ed45f395f51905f52019060208510610b94575b601f0160051c5f516020610ed45f395f51905f520190610e64565b876109bf565b5f516020610ed45f395f51905f529150610b73565b81355f516020610ef45f395f51905f528201558693506020909101906001016109a2565b610bf5905f516020610ef45f395f51905f5201865f516020610ef45f395f51905f5201610e64565b86610998565b634e487b7160e01b5f52604160045260245ffd5b60405162461bcd60e51b815260206004820152602b60248201527f456c656374696f6e3a2063616e6e6f74207075626c6973682074616c6c79206260448201526a65666f726520636c6f736560a81b6064820152608490fd5b506003544211610974565b60405162461bcd60e51b815260206004820152602260248201527f456c656374696f6e3a2063616c6c6572206e6f742074616c6c7956657269666960448201526132b960f11b6064820152608490fd5b346104d3575f3660031901126104d357602060ff60085460081c166040519015158152f35b346104d35760203660031901126104d3576004356009548110156104d35760209060095f525f516020610ef45f395f51905f520154604051908152f35b346104d3575f3660031901126104d3576020600354604051908152f35b346104d3575f3660031901126104d3576020600b54604051908152f35b346104d3575f3660031901126104d3576020600c54604051908152f35b346104d3575f3660031901126104d3576020907f00000000000000000000000000000000000000000000000000000000000000008152f35b90600182811c92168015610de2575b6020831014610dce57565b634e487b7160e01b5f52602260045260245ffd5b91607f1691610dc3565b90601f8019910116810190811067ffffffffffffffff821117610bfb57604052565b15610e1557565b60405162461bcd60e51b815260206004820152602160248201527f456c656374696f6e3a2074616c6c7920616c7265616479207075626c697368656044820152601960fa1b6064820152608490fd5b818110610e6f575050565b5f8155600101610e64565b6005546001600160a01b03163303610e8e57565b60405162461bcd60e51b815260206004820152601e60248201527f456c656374696f6e3a2063616c6c6572206e6f7420617574686f7269747900006044820152606490fdfec65a7bb8d6351c1cf70c95a316cc6a92839c986682d98bc35f958f4883f9d2a86e1540171b6c0c960b71a7020d9f60077f6af931a8bbf590da0223dacf75c7afa2646970667358221220f16e0563c7cfd058fb2ec835216bfe8050197c08a298a718deb712451ad1f1fb64736f6c634300081c0033a26469706673582212202aff0f13f6f992122bd8f7afa80ae6fdaf180e71b2403a09722e40221a5a0bb064736f6c634300081c0033\n";

    private static String librariesLinkedBinary;

    public static final String FUNC_CREATEELECTION = "createElection";

    public static final String FUNC_CREATIONPAUSED = "creationPaused";

    public static final String FUNC_ELECTIONCOUNT = "electionCount";

    public static final String FUNC_ELECTIONS = "elections";

    public static final String FUNC_ELECTORALAUTHORITY = "electoralAuthority";

    public static final String FUNC_GETELECTIONADDRESS = "getElectionAddress";

    public static final String FUNC_GLOBALCONFIG = "globalConfig";

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

    public RemoteFunctionCall<TransactionReceipt> createElection(String ipfsCid,
            byte[] metadataHash, BigInteger pkx, BigInteger pky, BigInteger startTime,
            BigInteger endTime, String tallyVerifier, String semaphore,
            BigInteger semaphoreGroupId) {
        final Function function = new Function(
                FUNC_CREATEELECTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ipfsCid), 
                new org.web3j.abi.datatypes.generated.Bytes32(metadataHash), 
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
