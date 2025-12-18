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
//import org.web3j.abi.datatypes.CustomError;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
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
public class TallyVerifier extends Contract {
    public static final String BINARY = "0x60803461019a57610e2890601f38839003908101601f19168201906001600160401b0382118383101761019e578083916040958694855283398101031261019a57610055602061004e836101b2565b92016101b2565b3315610183575f8054336001600160a01b03198083168217845586519590946001600160a01b03949093859391908416907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09080a360017f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f0055169384156101415750169182156100fd57816002541617600255600154161760015551610c6190816101c78239f35b835162461bcd60e51b815260206004820152601c60248201527f54616c6c7956657269666965723a207665726966696572207a65726f000000006044820152606490fd5b62461bcd60e51b815260206004820152601b60248201527f54616c6c7956657269666965723a20666163746f7279207a65726f00000000006044820152606490fd5b8251631e4fbdf760e01b81525f6004820152602490fd5b5f80fd5b634e487b7160e01b5f52604160045260245ffd5b51906001600160a01b038216820361019a5756fe6080806040526004361015610012575f80fd5b5f90813560e01c9081632b7ac3f314610b23575080635437988d14610a4c5780635bb4780814610973578063715018a614610919578063863bb0251461015b5780638da5cb5b14610134578063c45a01551461010b5763f2fde38b14610076575f80fd5b34610108576020366003190112610108576004356001600160a01b0381811691829003610104576100a5610b7a565b81156100eb575f54826bffffffffffffffffffffffff60a01b8216175f55167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e05f80a380f35b604051631e4fbdf760e01b815260048101849052602490fd5b8280fd5b80fd5b50346101085780600319360112610108576002546040516001600160a01b039091168152602090f35b5034610108578060031936011261010857546040516001600160a01b039091168152602090f35b50346101085760603660031901126101085760043567ffffffffffffffff81116109155761018d903690600401610b48565b9067ffffffffffffffff60243511610104573660236024350112156101045767ffffffffffffffff60243560040135116101045736602480356004013560051b81350101116101045760443567ffffffffffffffff8111610911576101f6903690600401610b48565b91909260027f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f0054146108ff5760027f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f00556001546001600160a01b031680156108ba576002546001600160a01b0316928315610875576002602435600401351061082157602435600401351561080d57602435600401356001101561080d5760446024350135156107c85760446024350135600201806002116107b457602435600401350361075a57604051630af34d3160e31b81526024803501356004820152602081602481885afa90811561074f578891610730575b50156106dc57602060249460405195868092636d5a875960e11b8252828035013560048301525afa9384156106d157879461068d575b506001600160a01b0384161561063c576103569060405193631e8e1e1360e01b8552604060048601526044850191610bf3565b6003198382030160248085019190915235600401358082526001600160fb1b03106106385760209183838180946024356004013560051b6024803501848301376024356004013560051b010301915afa90811561062d5785916105fe575b50156105b9576103c960446024350135610c13565b906103d76040519283610ba5565b60243560440135808352601f19906103ee90610c13565b01366020840137845b60446024350135811061054457506001600160a01b0381163b156105405790849060405192839163090496fd60e31b835260448301604060048501528151809152602060648501920190855b8181106105245750505083836104678193600319838203016024840152898b610bf3565b03926001600160a01b03165af18015610519576104e9575b507f1311dedb1cd8e009a0ad2d3d3eb24d37289dcfc2365bf3fb369b00f7133ab34960405160208152806104bf3395602480350135956020840191610bf3565b0390a360017f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f005580f35b67ffffffffffffffff819492941161050557604052915f61047f565b634e487b7160e01b82526041600452602482fd5b6040513d86823e3d90fd5b825184528a965087955060209384019390920191600101610443565b8480fd5b8060020160021161059157602435600401358160020110156105a55782518110156105a55760248160020160051b813501013560208260051b850101525f198114610591576001016103f7565b634e487b7160e01b86526011600452602486fd5b634e487b7160e01b86526032600452602486fd5b60405162461bcd60e51b815260206004820152601c60248201527f54616c6c7956657269666965723a20696e76616c69642070726f6f66000000006044820152606490fd5b610620915060203d602011610626575b6106188183610ba5565b810190610bdb565b5f6103b4565b503d61060e565b6040513d87823e3d90fd5b8680fd5b60405162461bcd60e51b8152602060048201526024808201527f54616c6c7956657269666965723a20656c656374696f6e2061646472657373206044820152637a65726f60e01b6064820152608490fd5b9093506020813d6020116106c9575b816106a960209383610ba5565b8101031261063857516001600160a01b038116810361063857925f610323565b3d915061069c565b6040513d89823e3d90fd5b60405162461bcd60e51b815260206004820152602660248201527f54616c6c7956657269666965723a20656c656374696f6e206e6f7420726567696044820152651cdd195c995960d21b6064820152608490fd5b610749915060203d602011610626576106188183610ba5565b5f6102ed565b6040513d8a823e3d90fd5b60405162461bcd60e51b815260206004820152602c60248201527f54616c6c7956657269666965723a207075626c69635369676e616c73206c656e60448201526b0cee8d040dad2e6dac2e8c6d60a31b6064820152608490fd5b634e487b7160e01b88526011600452602488fd5b60405162461bcd60e51b815260206004820152601e60248201527f54616c6c7956657269666965723a207a65726f2063616e6469646174657300006044820152606490fd5b634e487b7160e01b87526032600452602487fd5b60405162461bcd60e51b815260206004820152602660248201527f54616c6c7956657269666965723a207075626c69635369676e616c7320746f6f604482015265081cda1bdc9d60d21b6064820152608490fd5b60405162461bcd60e51b815260206004820152601e60248201527f54616c6c7956657269666965723a20666163746f7279206e6f742073657400006044820152606490fd5b60405162461bcd60e51b815260206004820152601f60248201527f54616c6c7956657269666965723a207665726966696572206e6f7420736574006044820152606490fd5b604051633ee5aeb560e01b8152600490fd5b8380fd5b5080fd5b5034610108578060031936011261010857610932610b7a565b80546001600160a01b03198116825581906001600160a01b03167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e08280a380f35b5034610108576020366003190112610108576004356001600160a01b03808216808303610911576109a2610b7a565b8015610a0757600280546001600160a01b031981169092179055604080516001600160a01b0393909216831682529190921660208301527f333c7678baf16017cf31e1d2f90143a62aab01a67a0807f6836a4304ceabb5559190819081015b0390a180f35b60405162461bcd60e51b815260206004820152601b60248201527f54616c6c7956657269666965723a20666163746f7279207a65726f00000000006044820152606490fd5b5034610108576020366003190112610108576004356001600160a01b0380821680830361091157610a7b610b7a565b8015610ade57600180546001600160a01b031981169092179055604080516001600160a01b0393909216831682529190921660208301527f0243549a92b2412f7a3caf7a2e56d65b8821b91345363faa5f57195384065fcc919081908101610a01565b60405162461bcd60e51b815260206004820152601c60248201527f54616c6c7956657269666965723a207665726966696572207a65726f000000006044820152606490fd5b9050346109155781600319360112610915576001546001600160a01b03168152602090f35b9181601f84011215610b765782359167ffffffffffffffff8311610b765760208381860195010111610b7657565b5f80fd5b5f546001600160a01b03163303610b8d57565b60405163118cdaa760e01b8152336004820152602490fd5b90601f8019910116810190811067ffffffffffffffff821117610bc757604052565b634e487b7160e01b5f52604160045260245ffd5b90816020910312610b7657518015158103610b765790565b908060209392818452848401375f828201840152601f01601f1916010190565b67ffffffffffffffff8111610bc75760051b6020019056fea26469706673582212202f740f7052f1c5e305d500cb23a1de113d4bb571f6882594e65ce7fde9c80fb464736f6c63430008140033\n";

    private static String librariesLinkedBinary;

    public static final String FUNC_FACTORY = "factory";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SETFACTORY = "setFactory";

    public static final String FUNC_SETVERIFIER = "setVerifier";

    public static final String FUNC_SUBMITTALLYPROOF = "submitTallyProof";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_VERIFIER = "verifier";

    public static final Event FACTORYUPDATED_EVENT = new Event("FactoryUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event TALLYPROOFSUBMITTED_EVENT = new Event("TallyProofSubmitted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event VERIFIERUPDATED_EVENT = new Event("VerifierUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

//    public static final CustomError OWNABLEINVALIDOWNER_ERROR = new CustomError("OwnableInvalidOwner",
//            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
//    ;
//
//    public static final CustomError OWNABLEUNAUTHORIZEDACCOUNT_ERROR = new CustomError("OwnableUnauthorizedAccount",
//            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
//    ;
//
//    public static final CustomError REENTRANCYGUARDREENTRANTCALL_ERROR = new CustomError("ReentrancyGuardReentrantCall",
//            Arrays.<TypeReference<?>>asList());
//    ;

    @Deprecated
    protected TallyVerifier(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TallyVerifier(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected TallyVerifier(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected TallyVerifier(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<String> factory() {
        final Function function = new Function(FUNC_FACTORY, 
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

    public RemoteFunctionCall<TransactionReceipt> setFactory(String _factory) {
        final Function function = new Function(
                FUNC_SETFACTORY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _factory)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setVerifier(String _verifier) {
        final Function function = new Function(
                FUNC_SETVERIFIER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _verifier)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> submitTallyProof(byte[] proof,
            List<BigInteger> publicSignals, String proofCid) {
        final Function function = new Function(
                FUNC_SUBMITTALLYPROOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicBytes(proof), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(publicSignals, org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.Utf8String(proofCid)), 
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

    public RemoteFunctionCall<String> verifier() {
        final Function function = new Function(FUNC_VERIFIER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static List<FactoryUpdatedEventResponse> getFactoryUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(FACTORYUPDATED_EVENT, transactionReceipt);
        ArrayList<FactoryUpdatedEventResponse> responses = new ArrayList<FactoryUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            FactoryUpdatedEventResponse typedResponse = new FactoryUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldFactory = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newFactory = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static FactoryUpdatedEventResponse getFactoryUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(FACTORYUPDATED_EVENT, log);
        FactoryUpdatedEventResponse typedResponse = new FactoryUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.oldFactory = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.newFactory = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<FactoryUpdatedEventResponse> factoryUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getFactoryUpdatedEventFromLog(log));
    }

    public Flowable<FactoryUpdatedEventResponse> factoryUpdatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(FACTORYUPDATED_EVENT));
        return factoryUpdatedEventFlowable(filter);
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

    public static List<TallyProofSubmittedEventResponse> getTallyProofSubmittedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TALLYPROOFSUBMITTED_EVENT, transactionReceipt);
        ArrayList<TallyProofSubmittedEventResponse> responses = new ArrayList<TallyProofSubmittedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TallyProofSubmittedEventResponse typedResponse = new TallyProofSubmittedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.electionId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.prover = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.proofCid = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TallyProofSubmittedEventResponse getTallyProofSubmittedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TALLYPROOFSUBMITTED_EVENT, log);
        TallyProofSubmittedEventResponse typedResponse = new TallyProofSubmittedEventResponse();
        typedResponse.log = log;
        typedResponse.electionId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.prover = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.proofCid = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<TallyProofSubmittedEventResponse> tallyProofSubmittedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTallyProofSubmittedEventFromLog(log));
    }

    public Flowable<TallyProofSubmittedEventResponse> tallyProofSubmittedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TALLYPROOFSUBMITTED_EVENT));
        return tallyProofSubmittedEventFlowable(filter);
    }

    public static List<VerifierUpdatedEventResponse> getVerifierUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VERIFIERUPDATED_EVENT, transactionReceipt);
        ArrayList<VerifierUpdatedEventResponse> responses = new ArrayList<VerifierUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VerifierUpdatedEventResponse typedResponse = new VerifierUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldVerifier = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newVerifier = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static VerifierUpdatedEventResponse getVerifierUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(VERIFIERUPDATED_EVENT, log);
        VerifierUpdatedEventResponse typedResponse = new VerifierUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.oldVerifier = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.newVerifier = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<VerifierUpdatedEventResponse> verifierUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getVerifierUpdatedEventFromLog(log));
    }

    public Flowable<VerifierUpdatedEventResponse> verifierUpdatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VERIFIERUPDATED_EVENT));
        return verifierUpdatedEventFlowable(filter);
    }

    @Deprecated
    public static TallyVerifier load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new TallyVerifier(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TallyVerifier load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TallyVerifier(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TallyVerifier load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new TallyVerifier(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TallyVerifier load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TallyVerifier(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TallyVerifier> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider, String _factory, String _verifier) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _factory), 
                new org.web3j.abi.datatypes.Address(160, _verifier)));
        return deployRemoteCall(TallyVerifier.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<TallyVerifier> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider,
            String _factory, String _verifier) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _factory), 
                new org.web3j.abi.datatypes.Address(160, _verifier)));
        return deployRemoteCall(TallyVerifier.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<TallyVerifier> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit, String _factory, String _verifier) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _factory), 
                new org.web3j.abi.datatypes.Address(160, _verifier)));
        return deployRemoteCall(TallyVerifier.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<TallyVerifier> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,
            String _factory, String _verifier) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _factory), 
                new org.web3j.abi.datatypes.Address(160, _verifier)));
        return deployRemoteCall(TallyVerifier.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
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

    public static class FactoryUpdatedEventResponse extends BaseEventResponse {
        public String oldFactory;

        public String newFactory;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class TallyProofSubmittedEventResponse extends BaseEventResponse {
        public BigInteger electionId;

        public String prover;

        public String proofCid;
    }

    public static class VerifierUpdatedEventResponse extends BaseEventResponse {
        public String oldVerifier;

        public String newVerifier;
    }
}
