package com.evote.backend.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
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
public class CommitmentRegistry extends Contract {
    public static final String BINARY = "0x608060405234801561001057600080fd5b50610386806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c806353f3eb8f1461003b578063e8fcf72314610057575b600080fd5b610055600480360381019061005091906101f1565b610087565b005b610071600480360381019061006c919061027c565b61019e565b60405161007e91906102b8565b60405180910390f35b6000801b6000803373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020541461010a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161010190610330565b60405180910390fd5b806000803373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055503373ffffffffffffffffffffffffffffffffffffffff167fab04abcbdf301b2cedb4d47c23eac5617749881ecc51c8886bdb3ce3f2038a5f8260405161019391906102b8565b60405180910390a250565b60006020528060005260406000206000915090505481565b600080fd5b6000819050919050565b6101ce816101bb565b81146101d957600080fd5b50565b6000813590506101eb816101c5565b92915050565b600060208284031215610207576102066101b6565b5b6000610215848285016101dc565b91505092915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006102498261021e565b9050919050565b6102598161023e565b811461026457600080fd5b50565b60008135905061027681610250565b92915050565b600060208284031215610292576102916101b6565b5b60006102a084828501610267565b91505092915050565b6102b2816101bb565b82525050565b60006020820190506102cd60008301846102a9565b92915050565b600082825260208201905092915050565b7f566f746520616c726561647920636f6d6d697474656400000000000000000000600082015250565b600061031a6016836102d3565b9150610325826102e4565b602082019050919050565b600060208201905081810360008301526103498161030d565b905091905056fea2646970667358221220f740616a40697ee8e40e8878e65df5340cf7766f886648a5149e3aba06b54cd264736f6c63430008130033";

    private static String librariesLinkedBinary;

    public static final String FUNC_COMMITMENTS = "commitments";

    public static final String FUNC_SUBMITCOMMITMENT = "submitCommitment";

    public static final Event COMMITMENTSUBMITTED_EVENT = new Event("CommitmentSubmitted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Bytes32>() {}));
    ;

    @Deprecated
    protected CommitmentRegistry(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CommitmentRegistry(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected CommitmentRegistry(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected CommitmentRegistry(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<CommitmentSubmittedEventResponse> getCommitmentSubmittedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(COMMITMENTSUBMITTED_EVENT, transactionReceipt);
        ArrayList<CommitmentSubmittedEventResponse> responses = new ArrayList<CommitmentSubmittedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CommitmentSubmittedEventResponse typedResponse = new CommitmentSubmittedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.voter = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.commitment = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CommitmentSubmittedEventResponse getCommitmentSubmittedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(COMMITMENTSUBMITTED_EVENT, log);
        CommitmentSubmittedEventResponse typedResponse = new CommitmentSubmittedEventResponse();
        typedResponse.log = log;
        typedResponse.voter = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.commitment = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<CommitmentSubmittedEventResponse> commitmentSubmittedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getCommitmentSubmittedEventFromLog(log));
    }

    public Flowable<CommitmentSubmittedEventResponse> commitmentSubmittedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(COMMITMENTSUBMITTED_EVENT));
        return commitmentSubmittedEventFlowable(filter);
    }

    public RemoteFunctionCall<byte[]> commitments(String param0) {
        final Function function = new Function(FUNC_COMMITMENTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<TransactionReceipt> submitCommitment(byte[] _commitment) {
        final Function function = new Function(
                FUNC_SUBMITCOMMITMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_commitment)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static CommitmentRegistry load(String contractAddress, Web3j web3j,
            Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CommitmentRegistry(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static CommitmentRegistry load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CommitmentRegistry(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static CommitmentRegistry load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider contractGasProvider) {
        return new CommitmentRegistry(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static CommitmentRegistry load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new CommitmentRegistry(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<CommitmentRegistry> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CommitmentRegistry.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<CommitmentRegistry> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CommitmentRegistry.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<CommitmentRegistry> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CommitmentRegistry.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<CommitmentRegistry> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CommitmentRegistry.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    // useless since we do not use libraries
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

    public static class CommitmentSubmittedEventResponse extends BaseEventResponse {
        public String voter;

        public byte[] commitment;
    }
}
