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
    public static final String BINARY = "6080346101ae57601f610c7e38819003918201601f19168301916001600160401b038311848410176101b25780849260409485528339810103126101ae57610052602061004b836101c6565b92016101c6565b331561019b575f8054336001600160a01b0319821681178355604051949290916001600160a01b0316907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09080a360017f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f00556001600160a01b031691821561015957506001600160a01b03169081156101145760018060a01b0319600254161760025560018060a01b03196001541617600155604051610aa390816101db8239f35b60405162461bcd60e51b815260206004820152601c60248201527f54616c6c7956657269666965723a207665726966696572207a65726f000000006044820152606490fd5b62461bcd60e51b815260206004820152601b60248201527f54616c6c7956657269666965723a20666163746f7279207a65726f00000000006044820152606490fd5b631e4fbdf760e01b5f525f60045260245ffd5b5f80fd5b634e487b7160e01b5f52604160045260245ffd5b51906001600160a01b03821682036101ae5756fe6080806040526004361015610012575f80fd5b5f905f3560e01c9081632b7ac3f314610988575080635437988d146108b55780635bb47808146107e1578063715018a61461078a578063863bb025146101545780638da5cb5b1461012d578063c45a0155146101045763f2fde38b14610076575f80fd5b34610101576020366003190112610101576004356001600160a01b038116908190036100ff576100a4610a47565b80156100eb5781546001600160a01b03198116821783556001600160a01b03167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e08380a380f35b631e4fbdf760e01b82526004829052602482fd5b505b80fd5b50346101015780600319360112610101576002546040516001600160a01b039091168152602090f35b5034610101578060031936011261010157546040516001600160a01b039091168152602090f35b50346104845760603660031901126104845760043567ffffffffffffffff8111610484576101869036906004016109ab565b6024359067ffffffffffffffff8211610484573660238301121561048457816004013567ffffffffffffffff81116104845760248301938160051b9336602486830101116104845760443567ffffffffffffffff8111610484576101ee9036906004016109ab565b96909460027f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f00541461077b5760027f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f00556001546001600160a01b0316968715610736576002546001600160a01b03169283156106f1576002871061069d5786156104c35780359887600110156104c35760448601359485156106585785600201806002116104d75789036105fe5760208b602460405180948193636d5a875960e11b835260048301525afa908115610460575f916105bc575b506001600160a01b031696871561056b576102fb9060405195631e8e1e1360e01b8752604060048801526044870191610a0f565b848103600319016024860152888152926001600160fb1b038911610484576020858286829694839585809a0137010301915afa908115610460575f91610530575b50156104eb5761034b81610a2f565b9361035960405195866109d9565b81855261036582610a2f565b602086019390601f19013685375f5b8381106104885750505050813b15610484576040805163090496fd60e31b81526004810191909152925160448401819052839160648301915f5b81811061046b5750505091815f816103d482966003198382030160248401528b8a610a0f565b03925af1801561046057610449575b5061041f7f1311dedb1cd8e009a0ad2d3d3eb24d37289dcfc2365bf3fb369b00f7133ab349916040519182916020835233966020840191610a0f565b0390a360017f9b779b17422d0df92223018b32b4d1fa46e071723d6817e2486d003becc55f005580f35b6104569194505f906109d9565b5f9261041f6103e3565b6040513d5f823e3d90fd5b82518452869450602093840193909201916001016103ae565b5f80fd5b80600201806002116104d757838110156104c35760249060051b8301019087518110156104c3576001913560208260051b8a01015201610374565b634e487b7160e01b5f52603260045260245ffd5b634e487b7160e01b5f52601160045260245ffd5b60405162461bcd60e51b815260206004820152601c60248201527f54616c6c7956657269666965723a20696e76616c69642070726f6f66000000006044820152606490fd5b90506020813d602011610563575b8161054b602093836109d9565b8101031261048457518015158103610484575f61033c565b3d915061053e565b60405162461bcd60e51b8152602060048201526024808201527f54616c6c7956657269666965723a20656c656374696f6e2061646472657373206044820152637a65726f60e01b6064820152608490fd5b90506020813d6020116105f6575b816105d7602093836109d9565b8101031261048457516001600160a01b0381168103610484575f6102c7565b3d91506105ca565b60405162461bcd60e51b815260206004820152602c60248201527f54616c6c7956657269666965723a207075626c69635369676e616c73206c656e60448201526b0cee8d040dad2e6dac2e8c6d60a31b6064820152608490fd5b60405162461bcd60e51b815260206004820152601e60248201527f54616c6c7956657269666965723a207a65726f2063616e6469646174657300006044820152606490fd5b60405162461bcd60e51b815260206004820152602660248201527f54616c6c7956657269666965723a207075626c69635369676e616c7320746f6f604482015265081cda1bdc9d60d21b6064820152608490fd5b60405162461bcd60e51b815260206004820152601e60248201527f54616c6c7956657269666965723a20666163746f7279206e6f742073657400006044820152606490fd5b60405162461bcd60e51b815260206004820152601f60248201527f54616c6c7956657269666965723a207665726966696572206e6f7420736574006044820152606490fd5b633ee5aeb560e01b5f5260045ffd5b34610484575f366003190112610484576107a2610a47565b5f80546001600160a01b0319811682556001600160a01b03167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e08280a3005b34610484576020366003190112610484576004356001600160a01b038116908181036104845761080f610a47565b811561087057600280546001600160a01b031981169093179055604080516001600160a01b0393841681529290911660208301527f333c7678baf16017cf31e1d2f90143a62aab01a67a0807f6836a4304ceabb5559190819081015b0390a1005b60405162461bcd60e51b815260206004820152601b60248201527f54616c6c7956657269666965723a20666163746f7279207a65726f00000000006044820152606490fd5b34610484576020366003190112610484576004356001600160a01b03811690818103610484576108e3610a47565b811561094357600180546001600160a01b031981169093179055604080516001600160a01b0393841681529290911660208301527f0243549a92b2412f7a3caf7a2e56d65b8821b91345363faa5f57195384065fcc91908190810161086b565b60405162461bcd60e51b815260206004820152601c60248201527f54616c6c7956657269666965723a207665726966696572207a65726f000000006044820152606490fd5b34610484575f366003190112610484576001546001600160a01b03168152602090f35b9181601f840112156104845782359167ffffffffffffffff8311610484576020838186019501011161048457565b90601f8019910116810190811067ffffffffffffffff8211176109fb57604052565b634e487b7160e01b5f52604160045260245ffd5b908060209392818452848401375f828201840152601f01601f1916010190565b67ffffffffffffffff81116109fb5760051b60200190565b5f546001600160a01b03163303610a5a57565b63118cdaa760e01b5f523360045260245ffdfea2646970667358221220810ea4fe140772ddc0ce4db74983abdfc550786ed9a62c9aefdcec2d04c336e364736f6c634300081c0033\n";

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
