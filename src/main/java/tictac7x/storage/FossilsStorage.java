package tictac7x.storage;

import net.runelite.api.Client;

public class FossilsStorage extends VarbitStorage {
    public FossilsStorage(
        final Client client,
        final StorageManager storages
    ) {
        super(
            client,
            storages,
            "fossils",
            24,
            0,
            new int[][]{
                // Unidentified fossils.
                {5828, 21562},
                {5829, 21564},
                {5830, 21566},
                {5831, 21568},

                // Small fossils.
                {5832, 21570},
                {5833, 21572},
                {5834, 21574},
                {5835, 21576},
                {5836, 21578},

                // Medium fossils.
                {5837, 21580},
                {5838, 21582},
                {5839, 21584},
                {5840, 21586},
                {5841, 21588},

                // Large fossils.
                {5842, 21590},
                {5843, 21592},
                {5844, 21594},
                {5845, 21596},
                {5846, 21598},

                // Plant fossils.
                {5847, 21600},
                {5848, 21602},
                {5849, 21604},
                {5850, 21606},
                {5851, 21608},
            }
        );
    }
}
