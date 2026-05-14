// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import {Test} from "forge-std/Test.sol";
import {stdJson} from "forge-std/StdJson.sol";
import {Election} from "../src/Election.sol";
import {Semaphore} from "@semaphore/contracts/Semaphore.sol";
import {SemaphoreVerifier} from "@semaphore/contracts/base/SemaphoreVerifier.sol";
import {ISemaphore} from "@semaphore/contracts/interfaces/ISemaphore.sol";
import {ISemaphoreGroups} from "@semaphore/contracts/interfaces/ISemaphoreGroups.sol";
import {ISemaphoreVerifier} from "@semaphore/contracts/interfaces/ISemaphoreVerifier.sol";

contract ElectionTest is Test {
    using stdJson for string;

    string private constant PROOF_FIXTURE_PATH =
        "test/fixtures/semaphore-proof.json";

    uint256 private constant ELECTION_ID = 1;
    uint256 private constant PKX = 111;
    uint256 private constant PKY = 222;
    uint256 private constant CANDIDATES_COUNT = 2;

    ISemaphoreVerifier private verifier;
    Semaphore private semaphore;
    Election private election;

    address private authority;
    address private tallyVerifier;
    address private outsider;

    bytes32 private metadataHash;
    uint256 private registrationStartTime;
    uint256 private registrationEndTime;
    uint256 private votingStartTime;
    uint256 private votingEndTime;

    struct ProofFixture {
        uint256 electionId;
        uint256 identityCommitment;
        uint256[] members;
        uint256[] c1x;
        uint256[] c1y;
        uint256[] c2x;
        uint256[] c2y;
        uint256 message;
        uint256 scope;
        uint256 merkleTreeDepth;
        uint256 merkleTreeRoot;
        uint256 nullifier;
        uint256[] points;
    }

    function setUp() public {
        vm.warp(1_700_000_000);

        authority = makeAddr("authority");
        tallyVerifier = makeAddr("tallyVerifier");
        outsider = makeAddr("outsider");

        metadataHash = keccak256(bytes("metadata"));
        registrationStartTime = block.timestamp + 10;
        registrationEndTime = registrationStartTime + 10;
        votingStartTime = registrationEndTime + 10;
        votingEndTime = votingStartTime + 10;

        verifier = ISemaphoreVerifier(address(new SemaphoreVerifier()));
        semaphore = new Semaphore(verifier);

        election = new Election(
            ELECTION_ID,
            metadataHash,
            PKX,
            PKY,
            registrationStartTime,
            registrationEndTime,
            votingStartTime,
            votingEndTime,
            CANDIDATES_COUNT,
            tallyVerifier,
            authority,
            address(semaphore)
        );
    }

    function test_constructor_sets_state_and_group() public view {
        assertEq(election.electionId(), ELECTION_ID);
        assertEq(election.metadataHash(), metadataHash);
        assertEq(election.publicKeyX(), PKX);
        assertEq(election.publicKeyY(), PKY);
        assertEq(election.registrationStartTime(), registrationStartTime);
        assertEq(election.registrationEndTime(), registrationEndTime);
        assertEq(election.votingStartTime(), votingStartTime);
        assertEq(election.votingEndTime(), votingEndTime);
        assertEq(election.candidatesCount(), CANDIDATES_COUNT);
        assertEq(election.tallyVerifier(), tallyVerifier);
        assertEq(election.electionAuthority(), authority);
        assertEq(election.semaphore(), address(semaphore));

        assertEq(semaphore.groupCounter(), 1);
        assertEq(election.semaphoreGroupId(), 0);
        assertEq(
            ISemaphoreGroups(address(semaphore)).getGroupAdmin(0),
            address(election)
        );
    }

    function test_constructor_reverts_on_bad_voting_window() public {
        vm.expectRevert("Election: bad window");
        new Election(
            ELECTION_ID,
            metadataHash,
            PKX,
            PKY,
            registrationStartTime,
            registrationEndTime,
            votingEndTime,
            votingEndTime,
            CANDIDATES_COUNT,
            tallyVerifier,
            authority,
            address(semaphore)
        );
    }

    function test_constructor_reverts_on_bad_registration_window() public {
        vm.expectRevert("Election: bad reg window");
        new Election(
            ELECTION_ID,
            metadataHash,
            PKX,
            PKY,
            registrationEndTime,
            registrationEndTime,
            votingStartTime,
            votingEndTime,
            CANDIDATES_COUNT,
            tallyVerifier,
            authority,
            address(semaphore)
        );
    }

    function test_constructor_reverts_on_registration_overlap() public {
        uint256 overlapStartTime = votingStartTime;
        uint256 overlapRegistrationEndTime = overlapStartTime + 1;

        vm.expectRevert("Election: reg overlaps voting");
        new Election(
            ELECTION_ID,
            metadataHash,
            PKX,
            PKY,
            registrationStartTime,
            overlapRegistrationEndTime,
            overlapStartTime,
            votingEndTime,
            CANDIDATES_COUNT,
            tallyVerifier,
            authority,
            address(semaphore)
        );
    }

    function test_constructor_reverts_on_zero_tally_verifier() public {
        vm.expectRevert("Election: zero tallyVerifier");
        new Election(
            ELECTION_ID,
            metadataHash,
            PKX,
            PKY,
            registrationStartTime,
            registrationEndTime,
            votingStartTime,
            votingEndTime,
            CANDIDATES_COUNT,
            address(0),
            authority,
            address(semaphore)
        );
    }

    function test_constructor_reverts_on_zero_authority() public {
        vm.expectRevert("Election: zero authority");
        new Election(
            ELECTION_ID,
            metadataHash,
            PKX,
            PKY,
            registrationStartTime,
            registrationEndTime,
            votingStartTime,
            votingEndTime,
            CANDIDATES_COUNT,
            tallyVerifier,
            address(0),
            address(semaphore)
        );
    }

    function test_constructor_reverts_on_zero_semaphore() public {
        vm.expectRevert("Election: zero semaphore");
        new Election(
            ELECTION_ID,
            metadataHash,
            PKX,
            PKY,
            registrationStartTime,
            registrationEndTime,
            votingStartTime,
            votingEndTime,
            CANDIDATES_COUNT,
            tallyVerifier,
            authority,
            address(0)
        );
    }

    function test_constructor_reverts_on_low_candidates() public {
        vm.expectRevert("Election: must have at least 2 candidates");
        new Election(
            ELECTION_ID,
            metadataHash,
            PKX,
            PKY,
            registrationStartTime,
            registrationEndTime,
            votingStartTime,
            votingEndTime,
            1,
            tallyVerifier,
            authority,
            address(semaphore)
        );
    }

    function test_registerVoter_onlyAuthority() public {
        vm.prank(outsider);
        vm.expectRevert("Election: caller not authority");
        election.registerVoter(123);
    }

    function test_registerVoter_requires_registration_period() public {
        vm.prank(authority);
        vm.expectRevert("Election: not in registration period");
        election.registerVoter(123);
    }

    function test_registerVoter_adds_member() public {
        uint256 identityCommitment = 123;

        _warpToRegistration();

        vm.expectEmit(true, false, false, true, address(election));
        emit Election.VoterRegistered(identityCommitment, authority);

        vm.prank(authority);
        election.registerVoter(identityCommitment);

        uint256 groupId = election.semaphoreGroupId();
        assertTrue(
            ISemaphoreGroups(address(semaphore)).hasMember(
                groupId,
                identityCommitment
            )
        );
        assertEq(
            ISemaphoreGroups(address(semaphore)).getMerkleTreeSize(groupId),
            1
        );
    }

    function test_setTallyVerifier_onlyAuthority() public {
        vm.prank(outsider);
        vm.expectRevert("Election: caller not authority");
        election.setTallyVerifier(makeAddr("newVerifier"));
    }

    function test_setTallyVerifier_rejects_zero_address() public {
        vm.prank(authority);
        vm.expectRevert("Election: zero address");
        election.setTallyVerifier(address(0));
    }

    function test_setTallyVerifier_updates_and_emits() public {
        address newVerifier = makeAddr("newVerifier");

        vm.expectEmit(true, true, false, true, address(election));
        emit Election.TallyVerifierUpdated(tallyVerifier, newVerifier);

        vm.prank(authority);
        election.setTallyVerifier(newVerifier);

        assertEq(election.tallyVerifier(), newVerifier);
    }

    function test_onTallyVerified_onlyTallyVerifier() public {
        vm.prank(outsider);
        vm.expectRevert("Election: caller not tallyVerifier");
        election.onTallyVerified(_tallies(), "cid", keccak256(bytes("proof")));
    }

    function test_onTallyVerified_requires_voting_closed() public {
        vm.prank(tallyVerifier);
        vm.expectRevert("Election: cannot publish tally before close");
        election.onTallyVerified(_tallies(), "cid", keccak256(bytes("proof")));
    }

    function test_onTallyVerified_requires_correct_length() public {
        _warpAfterVoting();

        uint256[] memory shortTallies = new uint256[](1);

        vm.prank(tallyVerifier);
        vm.expectRevert("Election: wrong tally length");
        election.onTallyVerified(
            shortTallies,
            "cid",
            keccak256(bytes("proof"))
        );
    }

    function test_onTallyVerified_updates_state_and_emits() public {
        _warpAfterVoting();

        uint256[] memory tallies = _tallies();
        bytes32 proofHash = keccak256(bytes("proof"));

        vm.expectEmit(false, false, false, true, address(election));
        emit Election.TallyPublished(ELECTION_ID, tallies, "cid");

        vm.prank(tallyVerifier);
        election.onTallyVerified(tallies, "cid", proofHash);

        assertEq(election.tallyProofHash(), proofHash);
        assertTrue(election.finalTallyPublished() > 0);

        uint256[] memory stored = election.getFinalTally();
        assertEq(stored.length, tallies.length);
        for (uint256 i = 0; i < tallies.length; i++) {
            assertEq(stored[i], tallies[i]);
        }
    }

    function test_onTallyVerified_rejects_double_publish() public {
        _warpAfterVoting();

        uint256[] memory tallies = _tallies();

        vm.prank(tallyVerifier);
        election.onTallyVerified(tallies, "cid", keccak256(bytes("proof")));

        vm.prank(tallyVerifier);
        vm.expectRevert("Election: tally already published");
        election.onTallyVerified(tallies, "cid", keccak256(bytes("proof-2")));
    }

    function test_getFinalTally_reverts_before_publish() public {
        vm.expectRevert("Election: tally not published");
        election.getFinalTally();
    }

    function test_getFinalTally_returns_after_publish() public {
        _warpAfterVoting();

        uint256[] memory tallies = _tallies();
        vm.prank(tallyVerifier);
        election.onTallyVerified(tallies, "cid", keccak256(bytes("proof")));

        uint256[] memory stored = election.getFinalTally();
        assertEq(stored.length, tallies.length);
        for (uint256 i = 0; i < tallies.length; i++) {
            assertEq(stored[i], tallies[i]);
        }
    }

    function test_submitVote_onlyAuthority() public {
        (
            uint256[] memory c1x,
            uint256[] memory c1y,
            uint256[] memory c2x,
            uint256[] memory c2y
        ) = _ciphertexts();

        ISemaphore.SemaphoreProof memory proof;

        vm.prank(outsider);
        vm.expectRevert("Election: caller not authority");
        election.submitVote(c1x, c1y, c2x, c2y, proof);
    }

    function test_submitVote_requires_started() public {
        (
            uint256[] memory c1x,
            uint256[] memory c1y,
            uint256[] memory c2x,
            uint256[] memory c2y
        ) = _ciphertexts();

        ISemaphore.SemaphoreProof memory proof;

        vm.prank(authority);
        vm.expectRevert("Election: not started");
        election.submitVote(c1x, c1y, c2x, c2y, proof);
    }

    function test_submitVote_requires_not_finished() public {
        (
            uint256[] memory c1x,
            uint256[] memory c1y,
            uint256[] memory c2x,
            uint256[] memory c2y
        ) = _ciphertexts();

        ISemaphore.SemaphoreProof memory proof;

        vm.warp(votingEndTime + 1);
        vm.prank(authority);
        vm.expectRevert("Election: finished");
        election.submitVote(c1x, c1y, c2x, c2y, proof);
    }

    function test_submitVote_requires_ciphertext_length() public {
        _warpToVoting();

        uint256[] memory c1x = new uint256[](1);
        uint256[] memory c1y = new uint256[](CANDIDATES_COUNT);
        uint256[] memory c2x = new uint256[](CANDIDATES_COUNT);
        uint256[] memory c2y = new uint256[](CANDIDATES_COUNT);

        ISemaphore.SemaphoreProof memory proof;

        vm.prank(authority);
        vm.expectRevert("Election: wrong cyphertext count");
        election.submitVote(c1x, c1y, c2x, c2y, proof);
    }

    function test_submitVote_rejects_bad_message() public {
        _warpToVoting();

        (
            uint256[] memory c1x,
            uint256[] memory c1y,
            uint256[] memory c2x,
            uint256[] memory c2y
        ) = _ciphertexts();

        ISemaphore.SemaphoreProof memory proof = _buildProof(0, ELECTION_ID);

        vm.prank(authority);
        vm.expectRevert("Election: bad message");
        election.submitVote(c1x, c1y, c2x, c2y, proof);
    }

    function test_submitVote_rejects_bad_scope() public {
        _warpToVoting();

        (
            uint256[] memory c1x,
            uint256[] memory c1y,
            uint256[] memory c2x,
            uint256[] memory c2y
        ) = _ciphertexts();

        uint256 expectedMessage = _expectedMessage(c1x, c1y, c2x, c2y);
        ISemaphore.SemaphoreProof memory proof = _buildProof(
            expectedMessage,
            999
        );

        vm.prank(authority);
        vm.expectRevert("Election: bad scope");
        election.submitVote(c1x, c1y, c2x, c2y, proof);
    }

    function test_submitVote_rejects_invalid_proof() public {
        uint256 identityCommitment = 123;
        _warpToRegistration();
        vm.prank(authority);
        election.registerVoter(identityCommitment);

        _warpToVoting();

        (
            uint256[] memory c1x,
            uint256[] memory c1y,
            uint256[] memory c2x,
            uint256[] memory c2y
        ) = _ciphertexts();

        uint256 expectedMessage = _expectedMessage(c1x, c1y, c2x, c2y);
        ISemaphore.SemaphoreProof memory proof = _buildProof(
            expectedMessage,
            ELECTION_ID
        );

        vm.prank(authority);
        vm.expectRevert(ISemaphore.Semaphore__InvalidProof.selector);
        election.submitVote(c1x, c1y, c2x, c2y, proof);
    }

    function test_submitVote_accepts_valid_proof() public {
        ProofFixture memory fixture = _loadProofFixture();

        assertEq(fixture.electionId, ELECTION_ID);
        assertEq(
            fixture.message,
            _expectedMessage(fixture.c1x, fixture.c1y, fixture.c2x, fixture.c2y)
        );
        assertEq(fixture.scope, ELECTION_ID);

        _warpToRegistration();
        _registerMembers(fixture.members);

        uint256 groupId = election.semaphoreGroupId();
        uint256 root = ISemaphoreGroups(address(semaphore)).getMerkleTreeRoot(
            groupId
        );
        assertEq(root, fixture.merkleTreeRoot);

        _warpToVoting();

        ISemaphore.SemaphoreProof memory proof = _toSemaphoreProof(fixture);

        vm.prank(authority);
        election.submitVote(
            fixture.c1x,
            fixture.c1y,
            fixture.c2x,
            fixture.c2y,
            proof
        );
    }

    function _warpToRegistration() internal {
        vm.warp(registrationStartTime + 1);
    }

    function _warpToVoting() internal {
        vm.warp(votingStartTime + 1);
    }

    function _warpAfterVoting() internal {
        vm.warp(votingEndTime + 1);
    }

    function _ciphertexts()
        internal
        pure
        returns (
            uint256[] memory c1x,
            uint256[] memory c1y,
            uint256[] memory c2x,
            uint256[] memory c2y
        )
    {
        c1x = new uint256[](CANDIDATES_COUNT);
        c1y = new uint256[](CANDIDATES_COUNT);
        c2x = new uint256[](CANDIDATES_COUNT);
        c2y = new uint256[](CANDIDATES_COUNT);

        for (uint256 i = 0; i < CANDIDATES_COUNT; i++) {
            c1x[i] = i + 1;
            c1y[i] = i + 11;
            c2x[i] = i + 21;
            c2y[i] = i + 31;
        }
    }

    function _expectedMessage(
        uint256[] memory c1x,
        uint256[] memory c1y,
        uint256[] memory c2x,
        uint256[] memory c2y
    ) internal pure returns (uint256) {
        bytes32 rawHash = keccak256(
            abi.encode(ELECTION_ID, c1x, c1y, c2x, c2y)
        );
        return uint256(rawHash) >> 8;
    }

    function _buildProof(
        uint256 message,
        uint256 scope
    ) internal view returns (ISemaphore.SemaphoreProof memory proof) {
        uint256 groupId = election.semaphoreGroupId();
        uint256 root = ISemaphoreGroups(address(semaphore)).getMerkleTreeRoot(
            groupId
        );

        uint256[8] memory points;

        proof = ISemaphore.SemaphoreProof({
            merkleTreeDepth: 1,
            merkleTreeRoot: root,
            nullifier: 123,
            message: message,
            scope: scope,
            points: points
        });
    }

    function _tallies() internal pure returns (uint256[] memory tallies) {
        tallies = new uint256[](CANDIDATES_COUNT);
        tallies[0] = 3;
        tallies[1] = 5;
    }

    function _loadProofFixture()
        internal
        view
        returns (ProofFixture memory fixture)
    {
        string memory json = vm.readFile(PROOF_FIXTURE_PATH);

        fixture.electionId = _parseUint(json.readString(".electionId"));
        fixture.identityCommitment = _parseUint(
            json.readString(".identityCommitment")
        );
        fixture.members = _parseUintArray(json.readStringArray(".members"));
        fixture.c1x = _parseUintArray(json.readStringArray(".c1x"));
        fixture.c1y = _parseUintArray(json.readStringArray(".c1y"));
        fixture.c2x = _parseUintArray(json.readStringArray(".c2x"));
        fixture.c2y = _parseUintArray(json.readStringArray(".c2y"));
        fixture.message = _parseUint(json.readString(".message"));
        fixture.scope = _parseUint(json.readString(".scope"));
        fixture.merkleTreeDepth = _parseUint(
            json.readString(".merkleTreeDepth")
        );
        fixture.merkleTreeRoot = _parseUint(
            json.readString(".merkleTreeRoot")
        );
        fixture.nullifier = _parseUint(json.readString(".nullifier"));
        fixture.points = _parseUintArray(json.readStringArray(".points"));
    }

    function _registerMembers(uint256[] memory members) internal {
        for (uint256 i = 0; i < members.length; i++) {
            vm.prank(authority);
            election.registerVoter(members[i]);
        }
    }

    function _toSemaphoreProof(
        ProofFixture memory fixture
    ) internal pure returns (ISemaphore.SemaphoreProof memory proof) {
        require(fixture.points.length == 8, "Fixture: points length");

        uint256[8] memory points;
        for (uint256 i = 0; i < 8; i++) {
            points[i] = fixture.points[i];
        }

        proof = ISemaphore.SemaphoreProof({
            merkleTreeDepth: fixture.merkleTreeDepth,
            merkleTreeRoot: fixture.merkleTreeRoot,
            nullifier: fixture.nullifier,
            message: fixture.message,
            scope: fixture.scope,
            points: points
        });
    }

    function _parseUint(string memory value) internal view returns (uint256) {
        return vm.parseUint(value);
    }

    function _parseUintArray(
        string[] memory values
    ) internal view returns (uint256[] memory parsed) {
        parsed = new uint256[](values.length);
        for (uint256 i = 0; i < values.length; i++) {
            parsed[i] = vm.parseUint(values[i]);
        }
    }
}
