package com.evote.backend.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.Bool;
// import org.web3j.abi.datatypes.CustomError;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
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
public class Semaphore extends Contract {
    public static final String BINARY = "608034607057601f611f4238819003918201601f19168301916001600160401b03831184841017607457808492602094604052833981010312607057516001600160a01b03811690819003607057600380546001600160a01b031916919091179055604051611eb990816100898239f35b5f80fd5b634e487b7160e01b5f52604160045260245ffdfe60806040526004361015610011575f80fd5b5f3560e01c8063042453711461169457806306dd8485146116725780631783efc3146114645780632b7ac3f31461143c5780632c880363146113fa5780634178c4d514610f7d578063456f418814610cc4578063568ee82614610c32578063575185ed14610c075780635c3f3b6014610bbc5780636389e10714610b905780636cdd32fe146106fc5780637ee35a0c146106d357806390509d441461069957806396324bd41461066f578063a9961c941461063d578063d0d898dd1461028e578063d24924fe14610271578063da3cda52146101ca578063dabc4d51146101895763fcf0b6ec14610100575f80fd5b346101855761010e36611be3565b5f828152600160205260409020546001600160a01b031633036101765760407f264b2a8f6763c084235fe832ba903482b2ef1a521336881fc75b987c2dfd29c591835f526004602052815f205490845f52600460205280835f205582519182526020820152a2005b6317737e4f60e31b5f5260045ffd5b5f80fd5b34610185576020366003190112610185576004355f525f60205260206101c260405f2060029060018101545f520160205260405f205490565b604051908152f35b34610185576020366003190112610185576004355f818152600260205260409020546001600160a01b03163303610262575f8181526001602090815260408083208054336001600160a01b0319808316821790935560029094529184208054909116905590926001600160a01b03909116917f0ba83579a0e79193ef649b9f5a8759d35af086ba62a3e207b52e4a8ae30d49e39080a4005b6334c4245d60e01b5f5260045ffd5b34610185575f366003190112610185576020600554604051908152f35b346101855761029c36611c0f565b815f526004602052600260405f200191604082013592835f5260205260ff60405f20541661062e575f818152600160205260409020546001600160a01b03161561061f57813592600184108015610615575b61060657815f525f60205260405f2054156105f757815f525f60205261032760405f2060029060018101545f520160205260405f205490565b92602081013593840361059b575b600354604051956001600160a01b039091169261035187611c4d565b61042e60a08401948535895260c085013560208a0152604051986103748a611c4d565b60405161038081611c4d565b60e0870135815261010087013560208201528a526040516103a081611c4d565b6101208701358152610140870135602082015260208b01526040516103c481611c4d565b61016087013581526101808701356020820152604051906103e482611c7d565b8a825285602083015260806060890135986103fe8a611da8565b604085015201359b61040f8d611da8565b606084015260405163a23f019960e01b81529586946004860190611cbb565b5f604485015b600282106105585750505061044d9060c4840190611cbb565b5f61010483015b6004821061053e575050506101a481602093876101848301525afa908115610533575f916104f8575b50156104e9577f0c32e14cfe81a05d371c248d22de6b7ae849e981b76a1f8842e7b6da73fc405a936101609361010092875f526004602052600260405f2001815f5260205260405f20600160ff19825416179055604051948552602085015260408401526060830137a4005b63012a9af160e61b5f5260045ffd5b90506020813d60201161052b575b8161051360209383611c99565b8101031261018557518015158103610185578861047d565b3d9150610506565b6040513d5f823e3d90fd5b829350602080916001939451815201930191018492610454565b909180939495505181905f915b600283106105855750505060206040600192019301910186949392610434565b6020806001928451815201920192019190610565565b825f526004602052600160405f2001845f5260205260405f2054835f52600460205260405f205481156105e8576105d191611c40565b421115610335575b6309581a9960e41b5f5260045ffd5b6326994ac360e11b5f5260045ffd5b63c8b02e0160e01b5f5260045ffd5b63767b278960e11b5f5260045ffd5b50602084116102ee565b63029f057960e01b5f5260045ffd5b63041162bd60e31b5f5260045ffd5b34610185576020366003190112610185576004355f526001602052602060018060a01b0360405f205416604051908152f35b34610185576020366003190112610185576004355f526004602052602060405f2054604051908152f35b346101855760206106c96106ac36611be3565b905f525f835260405f206003915f520160205260405f2054151590565b6040519015158152f35b34610185576020366003190112610185576004355f525f602052602060405f2054604051908152f35b346101855760603660031901126101855760243560043560443567ffffffffffffffff811161018557610733903690600401611bb2565b5f838152600160205260409020549093906001600160a01b0316330361017657825f525f6020526107678160405f20611cef565b90835f525f60205260405f209161078c82846003915f520160205260405f2054151590565b61079f57631c811d5b60e21b5f5260045ffd5b5f80526003830160205260405f2054610b81576107bc8284611cef565b5f9683918554965f198801978811610b6d576001870154975f93849392915b8a851061089a575050505050506108028460029060018101545f520160205260405f205490565b0361088b5760037f3108849c053c77b8073a11256dffb5ffd5b55e93e105a355e1c9061db890d8719386955f52600281016020528760405f2055835f52016020525f604081205561086a86604051938493846040919493926060820195825260208201520152565b0390a25f526004602052600160405f2001905f526020524260405f20555f80f35b631fd4986360e11b5f5260045ffd5b90919293949584861c6001808216145f146109f257505f516020611e645f395f51905f526108c9888587611dca565b3510156109e357610926602073__$3be769da771d2de143a9344a66e795cdbc$__9e8f604051916108f983611c4d565b6109048c898b611dca565b358352838301526040518080958194632b0aac7f60e11b835260048301611e36565b03915af4908115610533575f916109b0575b50610959916020919e6040519161094e83611c4d565b6109048b888a611dca565b03915af4908115610533575f9161097e575b50600180919701955b01939291906107db565b90506020813d82116109a8575b8161099860209383611c99565b810103126101855751600161096b565b3d915061098b565b90506020813d82116109db575b816109ca60209383611c99565b810103126101855751610959610938565b3d91506109bd565b6361c0541760e11b5f5260045ffd5b82871c14610b53575f516020611e645f395f51905f52610a13888587611dca565b3510156109e35760028a01865f52806020528160405f205414610b41575b50610a88602073__$3be769da771d2de143a9344a66e795cdbc$__9e8f60405191610a5b83611c4d565b8252610a688b888a611dca565b35838301526040518080958194632b0aac7f60e11b835260048301611e36565b03915af4908115610533575f91610b0e575b50610abd916020919e60405191610ab083611c4d565b8252610a688a8789611dca565b03915af4908115610533575f91610adc575b5060018091970195610974565b90506020813d8211610b06575b81610af660209383611c99565b8101031261018557516001610acf565b3d9150610ae9565b90506020813d8211610b39575b81610b2860209383611c99565b810103126101855751610abd610a9a565b3d9150610b1b565b865f526020528c60405f20558d610a31565b9594806001915f5260028b016020528d60405f2055610974565b634e487b7160e01b5f52601160045260245ffd5b6312c50cad60e11b5f5260045ffd5b34610185576020366003190112610185576004355f525f6020526020600160405f200154604051908152f35b34610185576020366003190112610185576020610bd7611bf9565b610bf060055491610be783611c32565b60055582611d2b565b805f5260048252610e1060405f2055604051908152f35b34610185575f366003190112610185576020600554610c2581611c32565b600555610bf03382611d2b565b34610185576040366003190112610185576024356001600160a01b0381169060043590829003610185575f818152600160205260409020546001600160a01b0316330361017657805f52600260205260405f20826bffffffffffffffffffffffff60a01b82541617905533907f1018365553cce55d9cb02ef73e18cc9311894f3fe1d1eafd235ac2d26cd8ba585f80a4005b3461018557610cd236611c0f565b5f828152600160205260409020549091906001600160a01b03161561061f578135600181108015610f73575b61060657815f525f60205260405f2054156105f757815f525f602052610d3760405f2060029060018101545f520160205260405f205490565b916020840135928303610f31575b5091610e3b9260018060a01b03600354169260405192610d6484611c4d565b60a0810135845260c08101356020850152604051610d8181611c4d565b604051610d8d81611c4d565b60e0830135815261010083013560208201528152604051610dad81611c4d565b610120830135815261014083013560208201526020820152610e1c608060405193610dd785611c4d565b6101608101358552610180810135602086015260405195610df787611c7d565b865260408101356020870152610e106060820135611da8565b60408701520135611da8565b606084015260405163a23f019960e01b81529687956004870190611cbb565b5f604486015b60028210610eec57505050610e5a9060c4850190611cbb565b5f61010484015b60048210610ed257505050816020936101a4926101848301525afa8015610533575f90610e96575b6040519015158152602090f35b506020813d602011610eca575b81610eb060209383611c99565b810103126101855751801515810361018557602090610e89565b3d9150610ea3565b825181528694506020928301926001929092019101610e61565b90918093949596505181905f915b60028310610f1b575050506020604060019201930191018795949392610e41565b6020806001928451815201920192019190610efa565b805f526004602052600160405f2001835f5260205260405f2054905f52600460205260405f205481156105e857610f6791611c40565b42116105d95783610d45565b5060208111610cfe565b346101855760803660031901126101855760443560043560243560643567ffffffffffffffff811161018557610fb7903690600401611bb2565b5f848152600160205260409020549094906001600160a01b0316330361017657835f525f602052610feb8360405f20611cef565b5f858152602081905260409020915f516020611e645f395f51905f52811061101c576361c0541760e11b5f5260045ffd5b5f85815260038401602052604090205461103f57631c811d5b60e21b5f5260045ffd5b5f818152600384016020526040902054610b815761105d8584611cef565b819786918554965f198801978811610b6d576001870154975f93849392915b8a8510611143575050505050506110a38460029060018101545f520160205260405f205490565b0361088b57859460036080947fea3588e4a2a0c93d6a0e69dfeaf7496f43ccccf02ad9ce0a5b7627cbca4b61b1965f52600281016020528960405f205583611128575b825f52016020525f604081205560405192835260208301526040820152856060820152a25f526004602052600160405f2001905f526020524260405f20555f80f35b5f8381528183016020526040808220548683529120556110e6565b9091929394958d85871c6001808216145f146112875750505f516020611e645f395f51905f52611174888587611dca565b3510156109e35773__$3be769da771d2de143a9344a66e795cdbc$__9d8e6040519161119f83611c4d565b6111aa8a8789611dca565b3583526020830152604051632b0aac7f60e11b815291829081906111d19060048301611e36565b03815a93602094f4908115610533575f91611254575b506111fd916020919f6040519161094e83611c4d565b03915af4908115610533575f91611222575b50600180919701955b019392919061107c565b90506020813d821161124c575b8161123c60209383611c99565b810103126101855751600161120f565b3d915061122f565b90506020813d821161127f575b8161126e60209383611c99565b8101031261018557516111fd6111e7565b3d9150611261565b83881c146113e057505f516020611e645f395f51905f526112a9888587611dca565b3510156109e35760028a01865f52806020528160405f2054146113ce575b5073__$3be769da771d2de143a9344a66e795cdbc$__9d8e604051916112ec83611c4d565b82526112f9898688611dca565b356020830152604051632b0aac7f60e11b8152918290819061131e9060048301611e36565b03815a93602094f4908115610533575f9161139b575b5061134a916020919f60405191610ab083611c4d565b03915af4908115610533575f91611369575b5060018091970195611218565b90506020813d8211611393575b8161138360209383611c99565b810103126101855751600161135c565b3d9150611376565b90506020813d82116113c6575b816113b560209383611c99565b81010312610185575161134a611334565b3d91506113a8565b865f526020528d60405f20558e6112c7565b956001919796815f5260028c0160205260405f2055611218565b34610185576040366003190112610185576020611415611bf9565b61142560055491610be783611c32565b805f526004825260243560405f2055604051908152f35b34610185575f366003190112610185576003546040516001600160a01b039091168152602090f35b346101855761147236611be3565b5f828152600160205260409020549091906001600160a01b03163303610176575f81815260208190526040902080549092905f516020611e645f395f51905f5282106114c7576361c0541760e11b5f5260045ffd5b816114db576314b48df160e11b5f5260045ffd5b5f828152600385016020526040902054610b8157835493600181019081549161150383611e06565b6001880190818911610b6d5710611662575b82905583955f5b83811061159557509160037f19239b3f93cd10558aaf11423af70c77763bf54f52bcc75bfa74d4d13548cde99492611555889795611c32565b928382555f52600281016020528860405f2055845f520160205260405f205561086a86604051938493846040919493926060820195825260208201520152565b96600180838a1c16145f146116495760206115e591604051906115b782611c4d565b8a5f5260028601835260405f205482528282015260405180938192632b0aac7f60e11b835260048301611e36565b038173__$3be769da771d2de143a9344a66e795cdbc$__5af48015610533575f90611617575b60019150975b0161151c565b506020813d8211611641575b8161163060209383611c99565b81010312610185576001905161160b565b3d9150611623565b96806001915f52600284016020528860405f2055611611565b9161166c90611c32565b91611515565b346101855760206101c261168536611be3565b905f525f835260405f20611cef565b346101855760403660031901126101855760043560243567ffffffffffffffff8111610185576116c8903690600401611bb2565b5f838152600160205260409020549092906001600160a01b03163303610176575f8181526020819052604081208054939091849060018201908183119060038601905b898110611b0f575050505061171f86611dee565b9261172d6040519485611c99565b868452600587901b93838501602082013682116101855785905b828210611aff5750505090600181019687545b61176381611e06565b61176d8b84611c40565b11156117815761177c90611c32565b61175a565b9190828a9955806117928a83611c40565b908260011c905f198301838111610b6d5760011c60018101809111610b6d57928c9796959493929192915f915b878310611871575050505050906117db6117f895600293611c40565b81556117e684611e15565b51925f520160205260405f2055611e15565b516040519182526060602083018190528201869052946001600160fb1b0310610185576080818486957f61e5e8054e3daf084a0c6c646c065e8bf5e7ca4d5567bda942309bd1652f349d95848401378760408301528101030190a25f526004602052600160405f2001905f526020524260405f20555f80f35b909294959697919398506118858386611ce2565b61188e81611dee565b9961189c6040519b8c611c99565b818b52601f196118ab83611dee565b013660208d01375f5b828110611961575050506001908116036119235780515f198101908111610b6d576118de91611e22565b51825f526002860160205260405f20555b600181901c5f198401848111610b6d5760011c60018101809111610b6d57600190930193908d9897969594919392936117bf565b805160018111611935575b50506118ef565b6001198101908111610b6d5761194a91611e22565b51825f526002860160205260405f20558c8061192e565b61196b8682611c40565b8060011b9080820460021490151715610b6d57821115611ac857865f5260028a0160205260405f2054905b5f6119a18883611c40565b8060011b9080820460021490151715610b6d5760018101809111610b6d578611611a86575b8d8115611a775750611a0292602091604051916119e283611c4d565b82528282015260405180948192632b0aac7f60e11b835260048301611e36565b038173__$3be769da771d2de143a9344a66e795cdbc$__5af4918215610533578d905f93611a40575b50611a398260019492611e22565b52016118b4565b9250506020823d8211611a6f575b81611a5b60209383611c99565b81010312610185579051908c611a39611a2b565b3d9150611a4e565b9050611a398260019492611e22565b50611a918782611c40565b8060011b9080820460021490151715610b6d5760018101809111610b6d57611abc84611ac292611ce2565b87611e22565b516119c6565b611ad28682611c40565b8060011b9080820460021490151715610b6d57611af283611af892611ce2565b86611e22565b5190611996565b8135815260209182019101611747565b5f516020611e645f395f51905f52611b28828c89611dca565b3510611b3d576361c0541760e11b5f5260045ffd5b611b48818b88611dca565b35611b5c576314b48df160e11b5f5260045ffd5b611b7f611b6a828c89611dca565b35886003915f520160205260405f2054151590565b610b815782610b6d5780611b9560019286611c40565b611ba0828d8a611dca565b355f528360205260405f20550161170b565b9181601f840112156101855782359167ffffffffffffffff8311610185576020808501948460051b01011161018557565b6040906003190112610185576004359060243590565b600435906001600160a01b038216820361018557565b906101c0600319830112610185576101a060043592602319011261018557602490565b5f198114610b6d5760010190565b91908201809211610b6d57565b6040810190811067ffffffffffffffff821117611c6957604052565b634e487b7160e01b5f52604160045260245ffd5b6080810190811067ffffffffffffffff821117611c6957604052565b90601f8019910116810190811067ffffffffffffffff821117611c6957604052565b905f905b60028210611ccc57505050565b6020806001928551815201930191019091611cbf565b91908203918211610b6d57565b60030190805f528160205260405f205415611d1c575f5260205260405f20545f198101908111610b6d5790565b631c811d5b60e21b5f5260045ffd5b5f81815260016020526040812080546001600160a01b0319166001600160a01b0385161790559091827ff0adfb94eab6daf835deb69c5738fe636150c3dfd08094a76f39b963dc8cb05a8380a26001600160a01b0316917f0ba83579a0e79193ef649b9f5a8759d35af086ba62a3e207b52e4a8ae30d49e38280a4565b6040516020810191825260208152611dc1604082611c99565b51902060081c90565b9190811015611dda5760051b0190565b634e487b7160e01b5f52603260045260245ffd5b67ffffffffffffffff8111611c695760051b60200190565b60ff8111610b6d576001901b90565b805115611dda5760200190565b8051821015611dda5760209160051b010190565b919060408301925f905b60028210611e4d57505050565b6020806001928551815201930191019091611e4056fe30644e72e131a029b85045b68181585d2833e84879b9709143e1f593f0000001a264697066735822122094a1d6320d17d013490535c23be47662097c5a0de2d21890b929b3a52b036e7f64736f6c634300081c0033\n";

    private static String librariesLinkedBinary;

    public static final String FUNC_ACCEPTGROUPADMIN = "acceptGroupAdmin";

    public static final String FUNC_ADDMEMBER = "addMember";

    public static final String FUNC_ADDMEMBERS = "addMembers";

    public static final String FUNC_createGroup = "createGroup";

    public static final String FUNC_GETGROUPADMIN = "getGroupAdmin";

    public static final String FUNC_GETMERKLETREEDEPTH = "getMerkleTreeDepth";

    public static final String FUNC_GETMERKLETREEROOT = "getMerkleTreeRoot";

    public static final String FUNC_GETMERKLETREESIZE = "getMerkleTreeSize";

    public static final String FUNC_GROUPCOUNTER = "groupCounter";

    public static final String FUNC_GROUPS = "groups";

    public static final String FUNC_HASMEMBER = "hasMember";

    public static final String FUNC_INDEXOF = "indexOf";

    public static final String FUNC_REMOVEMEMBER = "removeMember";

    public static final String FUNC_UPDATEGROUPADMIN = "updateGroupAdmin";

    public static final String FUNC_UPDATEGROUPMERKLETREEDURATION = "updateGroupMerkleTreeDuration";

    public static final String FUNC_UPDATEMEMBER = "updateMember";

    public static final String FUNC_VALIDATEPROOF = "validateProof";

    public static final String FUNC_VERIFIER = "verifier";

    public static final String FUNC_VERIFYPROOF = "verifyProof";

    public static final Event GROUPADMINPENDING_EVENT = new Event("GroupAdminPending", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event GROUPADMINUPDATED_EVENT = new Event("GroupAdminUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event GROUPCREATED_EVENT = new Event("GroupCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}));
    ;

    public static final Event GROUPMERKLETREEDURATIONUPDATED_EVENT = new Event("GroupMerkleTreeDurationUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event MEMBERADDED_EVENT = new Event("MemberAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event MEMBERREMOVED_EVENT = new Event("MemberRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event MEMBERUPDATED_EVENT = new Event("MemberUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event MEMBERSADDED_EVENT = new Event("MembersAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event PROOFVALIDATED_EVENT = new Event("ProofValidated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>(true) {}, new TypeReference<StaticArray8<Uint256>>() {}));
    ;

//    public static final CustomError LEAFALREADYEXISTS_ERROR = new CustomError("LeafAlreadyExists",
//            Arrays.<TypeReference<?>>asList());
//    ;
//
//    public static final CustomError LEAFCANNOTBEZERO_ERROR = new CustomError("LeafCannotBeZero",
//            Arrays.<TypeReference<?>>asList());
//    ;
//
//    public static final CustomError LEAFDOESNOTEXIST_ERROR = new CustomError("LeafDoesNotExist",
//            Arrays.<TypeReference<?>>asList());
//    ;
//
//    public static final CustomError LEAFGREATERTHANSNARKSCALARFIELD_ERROR = new CustomError("LeafGreaterThanSnarkScalarField",
//            Arrays.<TypeReference<?>>asList());
//    ;
//
//    public static final CustomError SEMAPHORE__CALLERISNOTTHEGROUPADMIN_ERROR = new CustomError("Semaphore__CallerIsNotTheGroupAdmin",
//            Arrays.<TypeReference<?>>asList());
//    ;
//
//    public static final CustomError SEMAPHORE__CALLERISNOTTHEPENDINGGROUPADMIN_ERROR = new CustomError("Semaphore__CallerIsNotThePendingGroupAdmin",
//            Arrays.<TypeReference<?>>asList());
//    ;
//
//    public static final CustomError SEMAPHORE__GROUPDOESNOTEXIST_ERROR = new CustomError("Semaphore__GroupDoesNotExist",
//            Arrays.<TypeReference<?>>asList());
//    ;
//
//    public static final CustomError SEMAPHORE__GROUPHASNOMEMBERS_ERROR = new CustomError("Semaphore__GroupHasNoMembers",
//            Arrays.<TypeReference<?>>asList());
//    ;
//
//    public static final CustomError SEMAPHORE__INVALIDPROOF_ERROR = new CustomError("Semaphore__InvalidProof",
//            Arrays.<TypeReference<?>>asList());
//    ;
//
//    public static final CustomError SEMAPHORE__MERKLETREEDEPTHISNOTSUPPORTED_ERROR = new CustomError("Semaphore__MerkleTreeDepthIsNotSupported",
//            Arrays.<TypeReference<?>>asList());
//    ;
//
//    public static final CustomError SEMAPHORE__MERKLETREEROOTISEXPIRED_ERROR = new CustomError("Semaphore__MerkleTreeRootIsExpired",
//            Arrays.<TypeReference<?>>asList());
//    ;
//
//    public static final CustomError SEMAPHORE__MERKLETREEROOTISNOTPARTOFTHEGROUP_ERROR = new CustomError("Semaphore__MerkleTreeRootIsNotPartOfTheGroup",
//            Arrays.<TypeReference<?>>asList());
//    ;
//
//    public static final CustomError SEMAPHORE__YOUAREUSINGTHESAMENULLIFIERTWICE_ERROR = new CustomError("Semaphore__YouAreUsingTheSameNullifierTwice",
//            Arrays.<TypeReference<?>>asList());
//    ;
//
//    public static final CustomError WRONGSIBLINGNODES_ERROR = new CustomError("WrongSiblingNodes",
//            Arrays.<TypeReference<?>>asList());
//    ;

    @Deprecated
    protected Semaphore(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Semaphore(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Semaphore(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Semaphore(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> acceptGroupAdmin(BigInteger groupId) {
        final Function function = new Function(
                FUNC_ACCEPTGROUPADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addMember(BigInteger groupId,
            BigInteger identityCommitment) {
        final Function function = new Function(
                FUNC_ADDMEMBER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId), 
                new org.web3j.abi.datatypes.generated.Uint256(identityCommitment)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addMembers(BigInteger groupId,
            List<BigInteger> identityCommitments) {
        final Function function = new Function(
                FUNC_ADDMEMBERS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(identityCommitments, org.web3j.abi.datatypes.generated.Uint256.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> createGroup(String admin,
            BigInteger merkleTreeDuration) {
        final Function function = new Function(
                FUNC_createGroup, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, admin), 
                new org.web3j.abi.datatypes.generated.Uint256(merkleTreeDuration)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> createGroup() {
        final Function function = new Function(
                FUNC_createGroup, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> createGroup(String admin) {
        final Function function = new Function(
                FUNC_createGroup, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, admin)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getGroupAdmin(BigInteger groupId) {
        final Function function = new Function(FUNC_GETGROUPADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> getMerkleTreeDepth(BigInteger groupId) {
        final Function function = new Function(FUNC_GETMERKLETREEDEPTH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getMerkleTreeRoot(BigInteger groupId) {
        final Function function = new Function(FUNC_GETMERKLETREEROOT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getMerkleTreeSize(BigInteger groupId) {
        final Function function = new Function(FUNC_GETMERKLETREESIZE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> groupCounter() {
        final Function function = new Function(FUNC_GROUPCOUNTER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> groups(BigInteger param0) {
        final Function function = new Function(FUNC_GROUPS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> hasMember(BigInteger groupId,
            BigInteger identityCommitment) {
        final Function function = new Function(FUNC_HASMEMBER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId), 
                new org.web3j.abi.datatypes.generated.Uint256(identityCommitment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> indexOf(BigInteger groupId,
            BigInteger identityCommitment) {
        final Function function = new Function(FUNC_INDEXOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId), 
                new org.web3j.abi.datatypes.generated.Uint256(identityCommitment)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> removeMember(BigInteger groupId,
            BigInteger identityCommitment, List<BigInteger> merkleProofSiblings) {
        final Function function = new Function(
                FUNC_REMOVEMEMBER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId), 
                new org.web3j.abi.datatypes.generated.Uint256(identityCommitment), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(merkleProofSiblings, org.web3j.abi.datatypes.generated.Uint256.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> updateGroupAdmin(BigInteger groupId,
            String newAdmin) {
        final Function function = new Function(
                FUNC_UPDATEGROUPADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId), 
                new org.web3j.abi.datatypes.Address(160, newAdmin)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> updateGroupMerkleTreeDuration(BigInteger groupId,
            BigInteger newMerkleTreeDuration) {
        final Function function = new Function(
                FUNC_UPDATEGROUPMERKLETREEDURATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId), 
                new org.web3j.abi.datatypes.generated.Uint256(newMerkleTreeDuration)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> updateMember(BigInteger groupId,
            BigInteger identityCommitment, BigInteger newIdentityCommitment,
            List<BigInteger> merkleProofSiblings) {
        final Function function = new Function(
                FUNC_UPDATEMEMBER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId), 
                new org.web3j.abi.datatypes.generated.Uint256(identityCommitment), 
                new org.web3j.abi.datatypes.generated.Uint256(newIdentityCommitment), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(merkleProofSiblings, org.web3j.abi.datatypes.generated.Uint256.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> validateProof(BigInteger groupId,
            SemaphoreProof proof) {
        final Function function = new Function(
                FUNC_VALIDATEPROOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId), 
                proof), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> verifier() {
        final Function function = new Function(FUNC_VERIFIER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> verifyProof(BigInteger groupId, SemaphoreProof proof) {
        final Function function = new Function(FUNC_VERIFYPROOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId), 
                proof), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public static List<GroupAdminPendingEventResponse> getGroupAdminPendingEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(GROUPADMINPENDING_EVENT, transactionReceipt);
        ArrayList<GroupAdminPendingEventResponse> responses = new ArrayList<GroupAdminPendingEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            GroupAdminPendingEventResponse typedResponse = new GroupAdminPendingEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.oldAdmin = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.newAdmin = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static GroupAdminPendingEventResponse getGroupAdminPendingEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(GROUPADMINPENDING_EVENT, log);
        GroupAdminPendingEventResponse typedResponse = new GroupAdminPendingEventResponse();
        typedResponse.log = log;
        typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.oldAdmin = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.newAdmin = (String) eventValues.getIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<GroupAdminPendingEventResponse> groupAdminPendingEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getGroupAdminPendingEventFromLog(log));
    }

    public Flowable<GroupAdminPendingEventResponse> groupAdminPendingEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GROUPADMINPENDING_EVENT));
        return groupAdminPendingEventFlowable(filter);
    }

    public static List<GroupAdminUpdatedEventResponse> getGroupAdminUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(GROUPADMINUPDATED_EVENT, transactionReceipt);
        ArrayList<GroupAdminUpdatedEventResponse> responses = new ArrayList<GroupAdminUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            GroupAdminUpdatedEventResponse typedResponse = new GroupAdminUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.oldAdmin = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.newAdmin = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static GroupAdminUpdatedEventResponse getGroupAdminUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(GROUPADMINUPDATED_EVENT, log);
        GroupAdminUpdatedEventResponse typedResponse = new GroupAdminUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.oldAdmin = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.newAdmin = (String) eventValues.getIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<GroupAdminUpdatedEventResponse> groupAdminUpdatedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getGroupAdminUpdatedEventFromLog(log));
    }

    public Flowable<GroupAdminUpdatedEventResponse> groupAdminUpdatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GROUPADMINUPDATED_EVENT));
        return groupAdminUpdatedEventFlowable(filter);
    }

    public static List<GroupCreatedEventResponse> getGroupCreatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(GROUPCREATED_EVENT, transactionReceipt);
        ArrayList<GroupCreatedEventResponse> responses = new ArrayList<GroupCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            GroupCreatedEventResponse typedResponse = new GroupCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static GroupCreatedEventResponse getGroupCreatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(GROUPCREATED_EVENT, log);
        GroupCreatedEventResponse typedResponse = new GroupCreatedEventResponse();
        typedResponse.log = log;
        typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<GroupCreatedEventResponse> groupCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getGroupCreatedEventFromLog(log));
    }

    public Flowable<GroupCreatedEventResponse> groupCreatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GROUPCREATED_EVENT));
        return groupCreatedEventFlowable(filter);
    }

    public static List<GroupMerkleTreeDurationUpdatedEventResponse> getGroupMerkleTreeDurationUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(GROUPMERKLETREEDURATIONUPDATED_EVENT, transactionReceipt);
        ArrayList<GroupMerkleTreeDurationUpdatedEventResponse> responses = new ArrayList<GroupMerkleTreeDurationUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            GroupMerkleTreeDurationUpdatedEventResponse typedResponse = new GroupMerkleTreeDurationUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.oldMerkleTreeDuration = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newMerkleTreeDuration = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static GroupMerkleTreeDurationUpdatedEventResponse getGroupMerkleTreeDurationUpdatedEventFromLog(
            Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(GROUPMERKLETREEDURATIONUPDATED_EVENT, log);
        GroupMerkleTreeDurationUpdatedEventResponse typedResponse = new GroupMerkleTreeDurationUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.oldMerkleTreeDuration = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.newMerkleTreeDuration = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<GroupMerkleTreeDurationUpdatedEventResponse> groupMerkleTreeDurationUpdatedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getGroupMerkleTreeDurationUpdatedEventFromLog(log));
    }

    public Flowable<GroupMerkleTreeDurationUpdatedEventResponse> groupMerkleTreeDurationUpdatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GROUPMERKLETREEDURATIONUPDATED_EVENT));
        return groupMerkleTreeDurationUpdatedEventFlowable(filter);
    }

    public static List<MemberAddedEventResponse> getMemberAddedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(MEMBERADDED_EVENT, transactionReceipt);
        ArrayList<MemberAddedEventResponse> responses = new ArrayList<MemberAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MemberAddedEventResponse typedResponse = new MemberAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.identityCommitment = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.merkleTreeRoot = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static MemberAddedEventResponse getMemberAddedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(MEMBERADDED_EVENT, log);
        MemberAddedEventResponse typedResponse = new MemberAddedEventResponse();
        typedResponse.log = log;
        typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.identityCommitment = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.merkleTreeRoot = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<MemberAddedEventResponse> memberAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getMemberAddedEventFromLog(log));
    }

    public Flowable<MemberAddedEventResponse> memberAddedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MEMBERADDED_EVENT));
        return memberAddedEventFlowable(filter);
    }

    public static List<MemberRemovedEventResponse> getMemberRemovedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(MEMBERREMOVED_EVENT, transactionReceipt);
        ArrayList<MemberRemovedEventResponse> responses = new ArrayList<MemberRemovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MemberRemovedEventResponse typedResponse = new MemberRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.identityCommitment = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.merkleTreeRoot = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static MemberRemovedEventResponse getMemberRemovedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(MEMBERREMOVED_EVENT, log);
        MemberRemovedEventResponse typedResponse = new MemberRemovedEventResponse();
        typedResponse.log = log;
        typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.identityCommitment = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.merkleTreeRoot = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<MemberRemovedEventResponse> memberRemovedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getMemberRemovedEventFromLog(log));
    }

    public Flowable<MemberRemovedEventResponse> memberRemovedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MEMBERREMOVED_EVENT));
        return memberRemovedEventFlowable(filter);
    }

    public static List<MemberUpdatedEventResponse> getMemberUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(MEMBERUPDATED_EVENT, transactionReceipt);
        ArrayList<MemberUpdatedEventResponse> responses = new ArrayList<MemberUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MemberUpdatedEventResponse typedResponse = new MemberUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.identityCommitment = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.newIdentityCommitment = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.merkleTreeRoot = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static MemberUpdatedEventResponse getMemberUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(MEMBERUPDATED_EVENT, log);
        MemberUpdatedEventResponse typedResponse = new MemberUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.identityCommitment = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.newIdentityCommitment = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.merkleTreeRoot = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
        return typedResponse;
    }

    public Flowable<MemberUpdatedEventResponse> memberUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getMemberUpdatedEventFromLog(log));
    }

    public Flowable<MemberUpdatedEventResponse> memberUpdatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MEMBERUPDATED_EVENT));
        return memberUpdatedEventFlowable(filter);
    }

    public static List<MembersAddedEventResponse> getMembersAddedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(MEMBERSADDED_EVENT, transactionReceipt);
        ArrayList<MembersAddedEventResponse> responses = new ArrayList<MembersAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MembersAddedEventResponse typedResponse = new MembersAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.startIndex = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.identityCommitments = (List<BigInteger>) ((Array) eventValues.getNonIndexedValues().get(1)).getNativeValueCopy();
            typedResponse.merkleTreeRoot = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static MembersAddedEventResponse getMembersAddedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(MEMBERSADDED_EVENT, log);
        MembersAddedEventResponse typedResponse = new MembersAddedEventResponse();
        typedResponse.log = log;
        typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.startIndex = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.identityCommitments = (List<BigInteger>) ((Array) eventValues.getNonIndexedValues().get(1)).getNativeValueCopy();
        typedResponse.merkleTreeRoot = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<MembersAddedEventResponse> membersAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getMembersAddedEventFromLog(log));
    }

    public Flowable<MembersAddedEventResponse> membersAddedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MEMBERSADDED_EVENT));
        return membersAddedEventFlowable(filter);
    }

    public static List<ProofValidatedEventResponse> getProofValidatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PROOFVALIDATED_EVENT, transactionReceipt);
        ArrayList<ProofValidatedEventResponse> responses = new ArrayList<ProofValidatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ProofValidatedEventResponse typedResponse = new ProofValidatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.merkleTreeRoot = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.scope = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.merkleTreeDepth = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.nullifier = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.message = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.points = (List<BigInteger>) ((Array) eventValues.getNonIndexedValues().get(3)).getNativeValueCopy();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ProofValidatedEventResponse getProofValidatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PROOFVALIDATED_EVENT, log);
        ProofValidatedEventResponse typedResponse = new ProofValidatedEventResponse();
        typedResponse.log = log;
        typedResponse.groupId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.merkleTreeRoot = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.scope = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
        typedResponse.merkleTreeDepth = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.nullifier = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.message = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.points = (List<BigInteger>) ((Array) eventValues.getNonIndexedValues().get(3)).getNativeValueCopy();
        return typedResponse;
    }

    public Flowable<ProofValidatedEventResponse> proofValidatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getProofValidatedEventFromLog(log));
    }

    public Flowable<ProofValidatedEventResponse> proofValidatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PROOFVALIDATED_EVENT));
        return proofValidatedEventFlowable(filter);
    }

    @Deprecated
    public static Semaphore load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new Semaphore(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Semaphore load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Semaphore(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Semaphore load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new Semaphore(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Semaphore load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Semaphore(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Semaphore> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider, String _verifier) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _verifier)));
        return deployRemoteCall(Semaphore.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<Semaphore> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider, String _verifier) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _verifier)));
        return deployRemoteCall(Semaphore.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Semaphore> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit, String _verifier) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _verifier)));
        return deployRemoteCall(Semaphore.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Semaphore> deploy(Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit, String _verifier) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _verifier)));
        return deployRemoteCall(Semaphore.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
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

    public static class GroupAdminPendingEventResponse extends BaseEventResponse {
        public BigInteger groupId;

        public String oldAdmin;

        public String newAdmin;
    }

    public static class GroupAdminUpdatedEventResponse extends BaseEventResponse {
        public BigInteger groupId;

        public String oldAdmin;

        public String newAdmin;
    }

    public static class GroupCreatedEventResponse extends BaseEventResponse {
        public BigInteger groupId;
    }

    public static class GroupMerkleTreeDurationUpdatedEventResponse extends BaseEventResponse {
        public BigInteger groupId;

        public BigInteger oldMerkleTreeDuration;

        public BigInteger newMerkleTreeDuration;
    }

    public static class MemberAddedEventResponse extends BaseEventResponse {
        public BigInteger groupId;

        public BigInteger index;

        public BigInteger identityCommitment;

        public BigInteger merkleTreeRoot;
    }

    public static class MemberRemovedEventResponse extends BaseEventResponse {
        public BigInteger groupId;

        public BigInteger index;

        public BigInteger identityCommitment;

        public BigInteger merkleTreeRoot;
    }

    public static class MemberUpdatedEventResponse extends BaseEventResponse {
        public BigInteger groupId;

        public BigInteger index;

        public BigInteger identityCommitment;

        public BigInteger newIdentityCommitment;

        public BigInteger merkleTreeRoot;
    }

    public static class MembersAddedEventResponse extends BaseEventResponse {
        public BigInteger groupId;

        public BigInteger startIndex;

        public List<BigInteger> identityCommitments;

        public BigInteger merkleTreeRoot;
    }

    public static class ProofValidatedEventResponse extends BaseEventResponse {
        public BigInteger groupId;

        public BigInteger merkleTreeRoot;

        public BigInteger scope;

        public BigInteger merkleTreeDepth;

        public BigInteger nullifier;

        public BigInteger message;

        public List<BigInteger> points;
    }
}
