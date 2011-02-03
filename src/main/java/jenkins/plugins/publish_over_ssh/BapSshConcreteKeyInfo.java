/*
 * The MIT License
 *
 * Copyright (C) 2010-2011 by Anthony Robinson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package jenkins.plugins.publish_over_ssh;

import hudson.Util;
import jenkins.plugins.publish_over.BPBuildInfo;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.Serializable;

public class BapSshConcreteKeyInfo implements Serializable, BapSshKeyInfo {
    
    private String passphrase;
    private String key;
    private String keyPath;

    public BapSshConcreteKeyInfo() {}
    
    @DataBoundConstructor
    public BapSshConcreteKeyInfo(String passphrase, String key, String keyPath) {
        this.passphrase = passphrase;
        this.key = key;
        this.keyPath = keyPath;
    }

    public String getPassphrase() { return passphrase; }
    public void setPassphrase(String passphrase) { this.passphrase = passphrase; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getKeyPath() { return keyPath; }
    public void setKeyPath(String keyPath) { this.keyPath = keyPath; }
    
    public byte[] getEffectiveKey(BPBuildInfo buildInfo) {
        if (hasKey())
            return BapSshUtil.toBytes(key);
        return buildInfo.readFileFromMaster(keyPath.trim());
    }

    public boolean useKey() {
        return hasKey() || hasKeyPath();
    }
    
    private boolean hasKey() {
        return Util.fixEmptyAndTrim(key) != null;
    }
    
    private boolean hasKeyPath() {
        return Util.fixEmptyAndTrim(keyPath) != null;
    }
    
    protected HashCodeBuilder createHashCodeBuilder() {
        return addToHashCode(new HashCodeBuilder());
    }

    protected HashCodeBuilder addToHashCode(HashCodeBuilder builder) {
        return builder.append(passphrase)
            .append(key)
            .append(keyPath);
    }
    
    protected EqualsBuilder createEqualsBuilder(BapSshConcreteKeyInfo that) {
        return addToEquals(new EqualsBuilder(), that);
    }
    
    protected EqualsBuilder addToEquals(EqualsBuilder builder, BapSshConcreteKeyInfo that) {
        return builder.append(passphrase, that.passphrase)
            .append(key, that.key)
            .append(keyPath, that.keyPath);
    }
    
    protected ToStringBuilder addToToString(ToStringBuilder builder) {
        return builder.append("passphrase", "***")
            .append("key", "***")
            .append("keyPath", keyPath);
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        return createEqualsBuilder((BapSshConcreteKeyInfo) o).isEquals();
    }

    public int hashCode() {
        return createHashCodeBuilder().toHashCode();
    }
    
    public String toString() {
        return addToToString(new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)).toString();
    }

}
