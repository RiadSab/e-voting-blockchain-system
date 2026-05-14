// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import {Test} from "forge-std/Test.sol";
import {ElectionFactory} from "../src/ElectionFactory.sol";
import {Election} from "../src/Election.sol";

contract MockSemaphore {
    uint256 public groupCounter;

    function createGroup(address) external returns (uint256) {
        groupCounter += 1;
        return groupCounter;
    }
}

contract ElectionFactoryTest is Test {
    ElectionFactory private factory;
    MockSemaphore private semaphore;

    address private authority;
    address private outsider;
    address private tallyVerifierAddr;
    address private electionAuthorityAddr;

    function setUp() public {
        authority = makeAddr("authority");
        outsider = makeAddr("outsider");
        tallyVerifierAddr = makeAddr("tallyVerifier");
        electionAuthorityAddr = makeAddr("electionAuthority");

        semaphore = new MockSemaphore();
        factory = new ElectionFactory(authority, address(semaphore));

        vm.warp(1_700_000_000);
    }

    function test_constructor_sets_initial_state() public {
        assertEq(factory.electionCount(), 1);
        assertEq(factory.electoralAuthority(), authority);
        assertEq(factory.semaphore(), address(semaphore));
    }

    function test_setElectoralAuthority_onlyAuthority() public {
        vm.prank(outsider);
        vm.expectRevert("Factory: caller not electoralAuthority");
        factory.setElectoralAuthority(makeAddr("newAuthority"));
    }

    function test_setElectoralAuthority_rejects_zero_address() public {
        vm.prank(authority);
        vm.expectRevert("Factory: authority zero address");
        factory.setElectoralAuthority(address(0));
    }

    function test_setElectoralAuthority_updates_value() public {
        address newAuthority = makeAddr("newAuthority");
        vm.prank(authority);
        factory.setElectoralAuthority(newAuthority);
        assertEq(factory.electoralAuthority(), newAuthority);
    }

    function test_createElection_onlyAuthority() public {
        (
            string memory ipfsCid,
            bytes32 metadataHash,
            uint256 pkx,
            uint256 pky,
            uint256 registrationStartTime,
            uint256 registrationEndTime,
            uint256 startTime,
            uint256 endTime,
            uint256 candidatesCount
        ) = _validParams();

        vm.prank(outsider);
        vm.expectRevert("Factory: caller not electoralAuthority");
        factory.createElection(
            ipfsCid,
            metadataHash,
            pkx,
            pky,
            registrationStartTime,
            registrationEndTime,
            startTime,
            endTime,
            candidatesCount,
            tallyVerifierAddr,
            electionAuthorityAddr
        );
    }

    function test_createElection_requires_ipfsCid() public {
        (
            ,
            bytes32 metadataHash,
            uint256 pkx,
            uint256 pky,
            uint256 registrationStartTime,
            uint256 registrationEndTime,
            uint256 startTime,
            uint256 endTime,
            uint256 candidatesCount
        ) = _validParams();

        vm.prank(authority);
        vm.expectRevert("Factory: ipfsCid required");
        factory.createElection(
            "",
            metadataHash,
            pkx,
            pky,
            registrationStartTime,
            registrationEndTime,
            startTime,
            endTime,
            candidatesCount,
            tallyVerifierAddr,
            electionAuthorityAddr
        );
    }

    function test_createElection_requires_valid_time_window() public {
        (
            string memory ipfsCid,
            bytes32 metadataHash,
            uint256 pkx,
            uint256 pky,
            uint256 registrationStartTime,
            uint256 registrationEndTime,
            uint256 startTime,
            uint256 endTime,
            uint256 candidatesCount
        ) = _validParams();

        vm.prank(authority);
        vm.expectRevert("Factory: invalid time window");
        factory.createElection(
            ipfsCid,
            metadataHash,
            pkx,
            pky,
            registrationStartTime,
            registrationEndTime,
            endTime,
            endTime,
            candidatesCount,
            tallyVerifierAddr,
            electionAuthorityAddr
        );
    }

    function test_createElection_requires_endTime_in_future() public {
        (
            string memory ipfsCid,
            bytes32 metadataHash,
            uint256 pkx,
            uint256 pky,
            uint256 registrationStartTime,
            uint256 registrationEndTime,
            ,
            ,
            uint256 candidatesCount
        ) = _validParams();

        uint256 startTime = block.timestamp - 10;
        uint256 endTime = block.timestamp;

        vm.prank(authority);
        vm.expectRevert("Factory: endTime in past");
        factory.createElection(
            ipfsCid,
            metadataHash,
            pkx,
            pky,
            registrationStartTime,
            registrationEndTime,
            startTime,
            endTime,
            candidatesCount,
            tallyVerifierAddr,
            electionAuthorityAddr
        );
    }

    function test_createElection_deploys_and_records_election() public {
        (
            string memory ipfsCid,
            bytes32 metadataHash,
            uint256 pkx,
            uint256 pky,
            uint256 registrationStartTime,
            uint256 registrationEndTime,
            uint256 startTime,
            uint256 endTime,
            uint256 candidatesCount
        ) = _validParams();

        vm.expectEmit(true, false, false, true, address(factory));
        emit ElectionFactory.ElectionCreated(
            1,
            address(0),
            metadataHash,
            ipfsCid,
            startTime,
            endTime
        );

        vm.prank(authority);
        uint256 newElectionId = factory.createElection(
            ipfsCid,
            metadataHash,
            pkx,
            pky,
            registrationStartTime,
            registrationEndTime,
            startTime,
            endTime,
            candidatesCount,
            tallyVerifierAddr,
            electionAuthorityAddr
        );

        assertEq(newElectionId, 1);
        assertEq(factory.electionCount(), 2);

        address electionAddress = factory.elections(newElectionId);
        assertTrue(electionAddress != address(0));

        Election election = Election(electionAddress);
        assertEq(election.electionId(), newElectionId);
        assertEq(election.metadataHash(), metadataHash);
        assertEq(election.publicKeyX(), pkx);
        assertEq(election.publicKeyY(), pky);
        assertEq(election.registrationStartTime(), registrationStartTime);
        assertEq(election.registrationEndTime(), registrationEndTime);
        assertEq(election.votingStartTime(), startTime);
        assertEq(election.votingEndTime(), endTime);
        assertEq(election.candidatesCount(), candidatesCount);
        assertEq(election.tallyVerifier(), tallyVerifierAddr);
        assertEq(election.electionAuthority(), electionAuthorityAddr);
        assertEq(election.semaphore(), address(semaphore));
        assertEq(semaphore.groupCounter(), 1);
        assertEq(election.semaphoreGroupId(), 1);
    }

    function test_createElection_increments_semaphore_group_ids() public {
        (
            string memory ipfsCid,
            bytes32 metadataHash,
            uint256 pkx,
            uint256 pky,
            uint256 registrationStartTime,
            uint256 registrationEndTime,
            uint256 startTime,
            uint256 endTime,
            uint256 candidatesCount
        ) = _validParams();

        vm.prank(authority);
        uint256 firstElectionId = factory.createElection(
            ipfsCid,
            metadataHash,
            pkx,
            pky,
            registrationStartTime,
            registrationEndTime,
            startTime,
            endTime,
            candidatesCount,
            tallyVerifierAddr,
            electionAuthorityAddr
        );

        vm.prank(authority);
        uint256 secondElectionId = factory.createElection(
            string.concat(ipfsCid, "-2"),
            keccak256(bytes("metadata-2")),
            pkx + 1,
            pky + 1,
            registrationStartTime,
            registrationEndTime,
            startTime,
            endTime,
            candidatesCount,
            tallyVerifierAddr,
            electionAuthorityAddr
        );

        Election firstElection = Election(factory.elections(firstElectionId));
        Election secondElection = Election(factory.elections(secondElectionId));

        assertEq(semaphore.groupCounter(), 2);
        assertEq(firstElection.semaphoreGroupId(), 1);
        assertEq(secondElection.semaphoreGroupId(), 2);
    }

    function _validParams()
        internal
        view
        returns (
            string memory ipfsCid,
            bytes32 metadataHash,
            uint256 pkx,
            uint256 pky,
            uint256 registrationStartTime,
            uint256 registrationEndTime,
            uint256 startTime,
            uint256 endTime,
            uint256 candidatesCount
        )
    {
        ipfsCid = "ipfs://example";
        metadataHash = keccak256(bytes("metadata"));
        pkx = 123;
        pky = 456;
        registrationStartTime = block.timestamp + 10;
        registrationEndTime = registrationStartTime + 10;
        startTime = registrationEndTime + 10;
        endTime = startTime + 10;
        candidatesCount = 2;
    }
}
