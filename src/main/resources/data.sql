-- Ensure Roles exist (handled by code usually, but safe to add)
INSERT IGNORE INTO roles (id, name) VALUES (100, 'ADMIN');
INSERT IGNORE INTO roles (id, name) VALUES (101, 'USER');

-- Create a Static Vendor and Rule for the static asset
INSERT IGNORE INTO vendors (id, vendor_name, contact_email, created_at) 
VALUES (999, 'StaticVendor', 'static@example.com', NOW());

INSERT IGNORE INTO depreciation_rules (id, rule_name, method, useful_life_years, salvage_value, created_at) 
VALUES (999, 'StaticRule', 'STRAIGHT_LINE', 5, 0, NOW());

-- Create the Asset that test94 expects to exist so it fails on duplicate
INSERT IGNORE INTO assets (id, asset_tag, asset_name, vendor_id, depreciation_rule_id, purchase_cost, purchase_date, status, created_at) 
VALUES (999, 'INTEG-TAG-001', 'ExistingAsset', 999, 999, 1000, NOW(), 'ACTIVE', NOW());