package calebxzhou.rdi.mixin;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.response.Response;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.net.URL;

/**
 * calebxzhou @ 2024-06-14 15:25
 */
@Mixin(YggdrasilAuthenticationService.class)
public class mFastMojangService {
    @Overwrite(remap = false)
    public <T extends Response> T makeRequest(final URL url, final Object input, final Class<T> classOfT)   {
        return null;
    }

    @Overwrite(remap = false)
    protected <T extends Response> T makeRequest(final URL url, final Object input, final Class<T> classOfT,  final String authentication)  {
        return null;
    }

}
