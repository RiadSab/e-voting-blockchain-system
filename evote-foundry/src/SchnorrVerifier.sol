// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

contract SchnorrVerifier {
    // BN254 scalar field order (the order of the group g generates).
    // The challenge c must be reduced mod this, matching Rust's from_be_bytes_mod_order.
    uint256 constant R =
        21888242871839275222246405745257275088548364400416034343698204186575808495617;

    // ecMul precompile (0x07): point * scalar
    function mul(uint256 x, uint256 y, uint256 s) internal view returns (uint256, uint256) {
        uint256[3] memory input = [x, y, s];
        uint256[2] memory out;
        bool ok;
        assembly { ok := staticcall(gas(), 0x07, input, 0x60, out, 0x40) }
        require(ok, "ecMul failed");
        return (out[0], out[1]);
    }

    // ecAdd precompile (0x06): point + point
    function add(uint256 x1, uint256 y1, uint256 x2, uint256 y2) internal view returns (uint256, uint256) {
        uint256[4] memory input = [x1, y1, x2, y2];
        uint256[2] memory out;
        bool ok;
        assembly { ok := staticcall(gas(), 0x06, input, 0x80, out, 0x40) }
        require(ok, "ecAdd failed");
        return (out[0], out[1]);
    }

    // Verify g*z == t + u*c, where c is recomputed here (never trusted as input).
    function verify(
        uint256 gx, uint256 gy,   // generator (will be (1,2))
        uint256 ux, uint256 uy,   // public key
        uint256 tx, uint256 ty,   // commitment
        uint256 z                 // response
    ) public view returns (bool) {
        // Recompute challenge: c = keccak256(g ‖ u ‖ t) mod R.
        // The points must be packed exactly as Rust hashed them: 32-byte big-endian x,y.
        uint256 c = uint256(
            keccak256(abi.encodePacked(gx, gy, ux, uy, tx, ty))
        ) % R;

        // left = g*z
        (uint256 lx, uint256 ly) = mul(gx, gy, z);

        // right = t + u*c
        (uint256 ucx, uint256 ucy) = mul(ux, uy, c);
        (uint256 rx, uint256 ry) = add(tx, ty, ucx, ucy);

        return lx == rx && ly == ry;
    }
}