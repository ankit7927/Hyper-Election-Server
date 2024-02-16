package com.x64tech.meserver.network;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.sdk.Enrollment;
import org.springframework.stereotype.Component;

import com.x64tech.meserver.configs.CustomUserDetails;
import com.x64tech.meserver.models.VoteModel;

import org.hyperledger.fabric.gateway.Identity;

@Component
public class Transaction {
    public final String NETWORK_NAME = "mychannel";
    public final String CONTRACT_NAME = "votetransfer";
    public final String DISPOSE = "disposeVotes";
    public final String GETSTATE = "getElectionState";
    public final String TRANSFER = "transferVote";

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }
    Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations",
            "org1.example.com", "connection-org1.yaml");

    private Gateway gatewayProvider() throws Exception {
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));
        if (wallet.get("admin") == null)
            throw new Exception("admin didn't exist..");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "admin").networkConfig(networkConfigPath)
                .discovery(true);
        return builder.connect();
    }

    private Gateway gatewayProvider(String username, String password) throws Exception {

        Enrollment enrollment = BCNetwork.getCAClient().enroll(username, password);

        Identity identity = Identities.newX509Identity("Org1MSP", enrollment);

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(identity).networkConfig(networkConfigPath)
                .discovery(true);
        return builder.connect();
    }

    public void transferVote(CustomUserDetails userDetails, String pass, VoteModel voteModel) throws Exception {
        Gateway gateway = gatewayProvider(userDetails.getUsername(), pass);
        Network network = gateway.getNetwork(NETWORK_NAME);
        Contract contract = network.getContract(CONTRACT_NAME);

        contract.submitTransaction(TRANSFER, userDetails.getUserID(),
                voteModel.getEleID(), voteModel.getCandID());
    }

    public String interactContract(String action, String electionID) throws Exception {
        Gateway gateway = gatewayProvider();
        Network network = gateway.getNetwork(NETWORK_NAME);
        Contract contract = network.getContract(CONTRACT_NAME);

        byte[] result = null;

        if (action.equals(DISPOSE))
            result = contract.submitTransaction(action, electionID);
        else if (action.equals(GETSTATE))
            result = contract.evaluateTransaction(action, electionID);
        else
            throw new Exception("bad contract action..");
            
        return new String(result);
    }
}
