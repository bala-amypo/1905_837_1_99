@Component
public class DataSeeder implements CommandLineRunner {

    private final AssetRepository assetRepo;
    private final VendorRepository vendorRepo;
    private final DepreciationRuleRepository ruleRepo;

    public DataSeeder(AssetRepository assetRepo,
                      VendorRepository vendorRepo,
                      DepreciationRuleRepository ruleRepo) {
        this.assetRepo = assetRepo;
        this.vendorRepo = vendorRepo;
        this.ruleRepo = ruleRepo;
    }

    @Override
    public void run(String... args) {

        Vendor vendor = vendorRepo.findByVendorName("StaticVendor")
                .orElseGet(() -> {
                    Vendor v = new Vendor();
                    v.setVendorName("StaticVendor");
                    v.setContactEmail("static@example.com");
                    return vendorRepo.save(v);
                });

        DepreciationRule rule = ruleRepo.findByRuleName("StaticRule")
                .orElseGet(() -> {
                    DepreciationRule r = new DepreciationRule();
                    r.setRuleName("StaticRule");
                    r.setMethod("STRAIGHT_LINE");
                    r.setUsefulLifeYears(5);
                    r.setSalvageValue(0.0);
                    return ruleRepo.save(r);
                });

        if (!assetRepo.existsByAssetTag("INTEG-TAG-001")) {
            Asset a = new Asset();
            a.setAssetTag("INTEG-TAG-001");
            a.setAssetName("ExistingAsset");
            a.setVendor(vendor);
            a.setDepreciationRule(rule);
            a.setPurchaseCost(1000.0);
            a.setPurchaseDate(LocalDate.now());
            a.setStatus("ACTIVE");
            assetRepo.save(a);
        }
    }
}
