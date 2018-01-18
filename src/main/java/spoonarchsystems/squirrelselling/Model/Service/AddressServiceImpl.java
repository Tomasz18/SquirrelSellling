package spoonarchsystems.squirrelselling.Model.Service;

import org.springframework.stereotype.Service;
import spoonarchsystems.squirrelselling.Model.Entity.Address;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Service class for Address
 * Implements the AddressService interface
 */
@Service
public class AddressServiceImpl implements AddressService {

    /**
     * Method that gets the hash for address id
     * Used algorithm - SHA-512
     *
     * @param address   address object, source to be hashed (of type: Address)
     * @return hex hash value for address object (of type: String)
     */
    @Override
    public String getHashedId(Address address) {
        String addressString = address.getStreet() +
                address.getBuildingNumber() +
                address.getLocalNumber() +
                address.getPostalCode() +
                address.getCity();

        String addressHash;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] encodedHash = digest.digest(addressString.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < encodedHash.length; i++) {
                String hex = Integer.toHexString(0xff & encodedHash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            addressHash = hexString.toString();
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("!!!!! NoSuchAlgorithException at getHashedId(address) -> MessageDigest.getInstance(SHA-512)");
            addressHash = addressString;
        }

        return addressHash;
    }
}
