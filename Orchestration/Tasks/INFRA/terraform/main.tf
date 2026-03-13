# Terraform: modular production foundation (optional cloud deploy).
# Use for LB + compute + DB + Redis in AWS/GCP/Azure; keep IaC DRY.
terraform {
  required_version = ">= 1.5"
  required_providers {
    # Example: aws. Uncomment and set backend when using.
    # aws = { source = "hashicorp/aws", version = "~> 5.0" }
  }
  # backend "s3" { bucket = "your-tf-state"; key = "release/terraform.tfstate"; region = "us-east-1"; }
}

# Modules (stub): wire in real modules for lb, web, db, redis.
# module "lb"    { source = "./modules/lb";    ... }
# module "web"   { source = "./modules/web";   ... }
# module "db"    { source = "./modules/db";    ... }
# module "redis" { source = "./modules/redis"; ... }

output "release_ready" {
  value       = "Run docker-compose in Orchestration/Tasks/INFRA/ for local production-like topology."
  description = "Placeholder until cloud modules are wired."
}
