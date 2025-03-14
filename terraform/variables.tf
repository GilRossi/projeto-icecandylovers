variable "ssh_private_key" {
  type        = string
  description = "Chave privada SSH para autenticação."
  sensitive   = true
}

variable "ssh_public_key" {
  type        = string
  description = "Chave pública SSH para acessar a instância."
  validation {
    condition     = length(var.ssh_public_key) > 0
    error_message = "A chave pública SSH não pode estar vazia."
  }
}

variable "aws_region" {
  description = "A região da AWS"
  type        = string
  default     = "us-east-1"
}