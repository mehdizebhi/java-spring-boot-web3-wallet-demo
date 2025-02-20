package dev.mehdizebhi.web3.configs;

import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.SPVBlockStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;

@Configuration
public class BitcoinConfig {

    @Bean
    @Profile("prod")
    public NetworkParameters mainNetParameters() {
        return MainNetParams.get();
    }

    @Bean
    @Profile(value = {"test", "dev"})
    public NetworkParameters testNetParameters() {
        return TestNet3Params.get();
    }

    @Bean
    public SPVBlockStore spvBlockStore(NetworkParameters params) throws BlockStoreException {
        File blockStoreFile = new File("bitcoin_blockstore.spv");
        return new SPVBlockStore(params, blockStoreFile);
    }

    @Bean
    public BlockChain blockChain(NetworkParameters params, SPVBlockStore spvBlockStore) throws BlockStoreException {
        return new BlockChain(params, spvBlockStore);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public PeerGroup peerGroup(NetworkParameters params, BlockChain chain) {
        var peerGroup = new PeerGroup(params, chain);
        peerGroup.addPeerDiscovery(new DnsDiscovery(params));
        return peerGroup;
    }
}